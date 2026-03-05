package io.github.doi02.ena.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Schema(description = "세션 응답")
public class SessionResponse {
    @Schema(description = "Access Token")
    private String accessToken;
}
