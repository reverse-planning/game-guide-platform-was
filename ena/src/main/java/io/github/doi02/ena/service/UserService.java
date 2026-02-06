package io.github.doi02.ena.service;

import io.github.doi02.ena.dto.user.SessionResponse;
import io.github.doi02.ena.entity.User;
import io.github.doi02.ena.repsository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 로그인 or 신규가입
    public SessionResponse loginOrRegister(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .nickname(nickname)
                            .build();
                    return userRepository.save(newUser);
                });
        return new SessionResponse(user.getId(), user.getNickname());
    }
}
