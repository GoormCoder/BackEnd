package goormcoder.webide.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindPasswordDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @Email(message = "유효한 이메일을 입력해주세요.")
    private String email;
}