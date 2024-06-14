package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;

public record QuestionFindDto(
        Long id,
        int questionNum,
        String questionTitle
) {
    public static QuestionFindDto of(Question question) {
        if (question == null) {
            return null;
        }
        return new QuestionFindDto(
                question.getId(),
                question.getQuestionNum(),
                question.getContent()
        );
    }
}
