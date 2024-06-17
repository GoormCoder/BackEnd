package goormcoder.webide.dto.request;

import goormcoder.webide.constants.BoardConstants;
import goormcoder.webide.domain.enumeration.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardCreateDto(

        BoardType boardType,
        @NotBlank(message = BoardConstants.TITLE_IS_BLANK)
        @Size(max = BoardConstants.TITLE_MAX_LENGTH, message = BoardConstants.TITLE_OVER_LENGTH)
        String title,
        @NotBlank(message = BoardConstants.CONTENT_IS_BLANK)
        String content,
        Long questionId
) {
}
