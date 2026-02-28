package io.github.doi02.ena.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/api/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ok");
    }
}