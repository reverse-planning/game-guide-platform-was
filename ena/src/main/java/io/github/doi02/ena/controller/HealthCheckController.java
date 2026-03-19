package io.github.doi02.ena.controller;

import io.github.doi02.ena.common.config.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Operation(summary = "서버 헬스체크", description = "서버 상태가 온전한지 확인합니다.")
    @GetMapping("/api/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ok");
    }
}