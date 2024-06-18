package goormcoder.webide.dto.request;

import goormcoder.webide.constants.AuthConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record MemberJoinDto(
        @NotBlank(message = AuthConstants.ID_IS_BLANK) 
        String loginId,

        @NotBlank(message = AuthConstants.EMAIL_IS_BLANK)
        @Email(message = AuthConstants.EMAIL_IS_WRONG) 
        String email,

        @NotBlank(message = AuthConstants.NAME_IS_BLANK) 
        String name,

        @NotNull(message = AuthConstants.AGE_IS_BLANK) 
        Integer age,

        @NotBlank(message = AuthConstants.GENDER_IS_BLANK) 
        String gender,

        @NotBlank(message = AuthConstants.PASSWORD_IS_BLANK)
        @Size(min = 6, message = AuthConstants.PASSWORD_IS_WRONG) 
        String password,

        @NotBlank(message = AuthConstants.ADDRESS_IS_BLANK) 
        String address
) {
}