package io.github.doi02.ena.service;

import io.github.doi02.ena.common.config.JwtTokenProvider; // 패키지 경로 확인 필요
import io.github.doi02.ena.common.exception.BusinessException;
import io.github.doi02.ena.common.exception.ErrorCode;
import io.github.doi02.ena.dto.user.LoginDto;
import io.github.doi02.ena.dto.user.SessionResponse;
import io.github.doi02.ena.entity.User;
import io.github.doi02.ena.repsository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public LoginDto loginOrRegister(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .nickname(nickname)
                            .build();
                    return userRepository.save(newUser);
                });

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getNickname());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // Redis 저장
        redisTemplate.opsForValue().set(
                "RT:" + user.getId(),
                refreshToken,
                7,
                TimeUnit.DAYS
        );

        // LoginDto에 모든 필드를 담아서 controller로 전달 (이후 refresh토큰은 쿠키로)
        return new LoginDto(user.getId(), user.getNickname(), accessToken, refreshToken);
    }

    // 토큰 재발급
    @Transactional
    public LoginDto reissue(String refreshToken) {
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰에서 User ID 추출
        Long userId = Long.valueOf(jwtTokenProvider.getSubject(refreshToken));

        // Redis에서 해당 유저의 Refresh Token 가져오기
        String savedToken = redisTemplate.opsForValue().get("RT:" + userId);

        // Redis에 토큰이 없거나, 클라이언트가 보낸 토큰과 일치하지 않으면 예외 발생
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 새로운 토큰들 생성 (Access, Refresh 둘다 새로 생성함)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getNickname());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // Redis 업데이트
        redisTemplate.opsForValue().set(
                "RT:" + user.getId(),
                newRefreshToken,
                7,
                TimeUnit.DAYS
        );

        return new LoginDto(user.getId(), user.getNickname(), newAccessToken, newRefreshToken);
    }
}