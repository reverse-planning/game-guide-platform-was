package io.github.doi02.ena.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "엑세스 토큰 유효 응답")
public class AccessResponse {
    @Schema(description = "Nickname")
    private String nickname;
}
