package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;

public record QuestionDto(
        Long id,
        Integer level,
        String title
) {
    public static QuestionDto of(Question question) {
        if (question == null) {
            return null;
        }
        return new QuestionDto(
                question.getId(),
                question.getLevel(),
                question.getTitle()
        );
    }
}
