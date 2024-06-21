package goormcoder.webide.dto.request;

import goormcoder.webide.constants.TagConstants;
import jakarta.validation.constraints.NotBlank;

public record QuestionTagCreateDto(
        @NotBlank(message = TagConstants.NAME_IS_BLANK)
        String name
) {

}
