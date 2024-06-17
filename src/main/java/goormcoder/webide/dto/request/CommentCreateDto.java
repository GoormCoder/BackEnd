package goormcoder.webide.dto.request;

import goormcoder.webide.constants.CommentConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateDto(

        @NotBlank(message = CommentConstants.COMMENT_IS_BLANK)
        @Size(max = CommentConstants.COMMENT_MAX_LENGTH, message = CommentConstants.COMMENT_OVER_LENGTH)
        String content
) {
}
