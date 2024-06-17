package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;

public record QuestionFindDto(
        Long id,
        Integer level,
        String title,
        String content
) {
    public static QuestionFindDto of(Question question) {
        return new QuestionFindDto(
                question.getId(),
                question.getLevel(),
                question.getTitle(),
                question.getContent()
        );
    }
}
