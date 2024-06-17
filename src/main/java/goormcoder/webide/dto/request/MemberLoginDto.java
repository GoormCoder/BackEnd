package goormcoder.webide.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginDto(

        @NotBlank(message = "아이디는 필수 입력입니다.")
        String loginId,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        String password

) {

}
