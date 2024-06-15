package goormcoder.webide.dto.request;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenDto(

        @NotNull(message = "Token 정보가 없습니다.")
        String token

) {
}
