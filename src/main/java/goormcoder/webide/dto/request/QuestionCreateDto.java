package goormcoder.webide.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record QuestionCreateDto (
        String title,

        @Min(value = 0, message = "문제 레벨은 0 이상 3 이하이어야 합니다.")
        @Max(value = 3, message = "문제 레벨은 0 이상 3 이하이어야 합니다.")
        int level,

        String content
) {

}
