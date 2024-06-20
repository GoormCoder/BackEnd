package goormcoder.webide.dto.request;

import java.util.List;

public record QuestionTagsUpdateDto (
        List<Long> tagIds
) {

}
