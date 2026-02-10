package io.github.doi02.ena.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "세션 요청")
public class SessionRequest {
    private String nickname;
}
