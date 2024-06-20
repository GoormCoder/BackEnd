package goormcoder.webide.dto.response;

import goormcoder.webide.domain.QuestionTag;

public record QuestionTagSummaryDto(
        Long id,
        String name
) {

    public static QuestionTagSummaryDto of (QuestionTag tag) {
        return new QuestionTagSummaryDto(tag.getId(), tag.getName());
    }

}
