package io.github.doi02.ena.controller;

import io.github.doi02.ena.dto.user.SessionRequest;
import io.github.doi02.ena.dto.user.SessionResponse;
import io.github.doi02.ena.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 로그인 기능
    @PostMapping("/session")
    ResponseEntity<SessionResponse> createSession(@RequestBody SessionRequest request) {
        SessionResponse response = userService.loginOrRegister(request.getNickname());
        return ResponseEntity.ok(response);
    }
}
