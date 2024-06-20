package goormcoder.webide.dto.response;

import goormcoder.webide.domain.QuestionTag;
import java.util.List;

public record QuestionTagSummaryDto(
        Long id,
        String name
) {

    public static QuestionTagSummaryDto of (QuestionTag tag) {
        return new QuestionTagSummaryDto(tag.getId(), tag.getName());
    }

    public static List<QuestionTagSummaryDto> listOf (List<QuestionTag> tags) {
        return tags.stream()
                .map(QuestionTagSummaryDto::of)
                .toList();
    }

}
