package goormcoder.webide.dto.request;

import goormcoder.webide.constants.QuestionConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record QuestionCreateDto (
        String title,

        @Min(value = QuestionConstants.MIN_LEVEL, message = QuestionConstants.LEVEL_OUT_OF_RANGE)
        @Max(value = QuestionConstants.MAX_LEVEL, message = QuestionConstants.LEVEL_OUT_OF_RANGE)
        int level,

        String content
) {

}
