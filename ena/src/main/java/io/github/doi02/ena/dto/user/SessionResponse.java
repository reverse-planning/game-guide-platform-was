package io.github.doi02.ena.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class SessionResponse {
    private Long userId;
    private String nickname;
}
