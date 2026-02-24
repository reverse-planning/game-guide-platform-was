package io.github.doi02.ena.controller;

import io.github.doi02.ena.dto.user.LoginDto;
import io.github.doi02.ena.dto.user.SessionRequest;
import io.github.doi02.ena.dto.user.SessionResponse;
import io.github.doi02.ena.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "사용자 로그인", description = "사용자가 게시판 사이트에 로그인합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @PostMapping("/session")
    public ResponseEntity<SessionResponse> createSession(
            @RequestBody SessionRequest request,
            HttpServletResponse response) {

        // 서비스 호출하여 DTO 받아오기
        LoginDto loginDto = userService.loginOrRegister(request.getNickname());

        // Refresh Token을 담은 HttpOnly 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("refreshToken", loginDto.getRefreshToken())
                .httpOnly(true)
                .secure(false) // 로컬(HTTP) 테스트를 위해 잠시 false 배포시 true로 고치기
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("Lax")
                .build();

        // 응답 헤더에 쿠키 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Body에는 Access Token과 유저 정보만 담아서 전송 (refresh token빼고)
        return ResponseEntity.ok(new SessionResponse(
                loginDto.getUserId(),
                loginDto.getNickname(),
                loginDto.getAccessToken()
        ));
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token을 이용해 Access Token을 재발급합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    @PostMapping("/reissue")
    public ResponseEntity<SessionResponse> reissue(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {
        LoginDto loginDto = userService.reissue(refreshToken);

        // 새로운 Refresh Token을 쿠키에 담기
        ResponseCookie cookie = ResponseCookie.from("refreshToken", loginDto.getRefreshToken())
                .httpOnly(true)
                .secure(false) // 배포 시 true
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 새로운 Access Token 반환
        return ResponseEntity.ok(new SessionResponse(
                loginDto.getUserId(),
                loginDto.getNickname(),
                loginDto.getAccessToken()
        ));
    }
}