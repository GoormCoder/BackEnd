package goormcoder.webide.dto.request;

import goormcoder.webide.constants.AuthConstants;
import jakarta.validation.constraints.NotBlank;

public record MemberLoginDto(

        @NotBlank(message = AuthConstants.ID_IS_BLANK)
        String loginId,

        @NotBlank(message = AuthConstants.PASSWORD_IS_BLANK)
        String password

) {

}
