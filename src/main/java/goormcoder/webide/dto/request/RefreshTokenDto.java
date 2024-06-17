package goormcoder.webide.dto.request;

import goormcoder.webide.constants.AuthConstants;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenDto(

        @NotNull(message = AuthConstants.TOKEN_IS_NULL)
        String token

) {
}
