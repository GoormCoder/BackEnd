package goormcoder.webide.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateDto(

        @NotBlank(message = "댓글 내용은 필수로 입력해야 합니다.")
        @Size(max = 4000, message = "댓글 내용이 최대 글자 수(4000자)를 초과했습니다.")
        String content
) {
}
