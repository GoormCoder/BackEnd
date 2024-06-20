package goormcoder.webide.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindLoginIdDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email(message = "유효한 이메일을 입력해주세요.")
    private String email;
}
