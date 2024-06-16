package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;

public record QuestionFindDto(
        Long questionNum,
        String questionTitle
) {
    public static QuestionFindDto of(Question question) {
        if (question == null) {
            return null;
        }
        return new QuestionFindDto(
                question.getQuestionNum(),
                question.getContent()
        );
    }
}
