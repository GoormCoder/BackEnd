package goormcoder.webide.dto.request;

import goormcoder.webide.domain.enums.BoardType;

public record BoardCreateDto(

        Long memberId,
        BoardType boardType,
        String title,
        String content,
        Integer questionNum
) {
}
