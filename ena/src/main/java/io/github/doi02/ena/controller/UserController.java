package io.github.doi02.ena.controller;

import io.github.doi02.ena.dto.user.SessionRequest;
import io.github.doi02.ena.dto.user.SessionResponse;
import io.github.doi02.ena.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 로그인 기능
    @Operation(
            summary = "사용자 로그인",
            description = "사용자가 게시판 사이트에 로그인합니다."
    )
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    //@ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
    @PostMapping("/session")
    ResponseEntity<SessionResponse> createSession(@RequestBody SessionRequest request) {
        SessionResponse response = userService.loginOrRegister(request.getNickname());
        return ResponseEntity.ok(response);
    }
}
