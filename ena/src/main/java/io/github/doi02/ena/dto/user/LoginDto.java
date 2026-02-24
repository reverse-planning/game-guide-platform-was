package io.github.doi02.ena.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {
    private Long userId;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}