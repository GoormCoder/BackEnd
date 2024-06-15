package goormcoder.webide.dto.request;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenDto(

        @NotNull
        String token

) {
}
