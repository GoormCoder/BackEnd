package goormcoder.webide.dto.request;

import goormcoder.webide.domain.enumeration.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardCreateDto(

        BoardType boardType,
        @NotBlank(message = "게시글 제목은 필수로 입력해야 합니다.")
        @Size(max = 300, message = "게시글 제목이 최대 글자 수(300자)를 초과했습니다.")
        String title,
        @NotBlank(message = "게시글 내용은 필수로 입력해야 합니다.")
        String content,
        Integer questionNum
) {
}
