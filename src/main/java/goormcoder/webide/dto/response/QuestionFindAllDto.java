package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;

import java.util.List;

public record QuestionFindAllDto(
        long questionNum,
        String questionTitle,
        int questionLevel
) {
        public static List<QuestionFindAllDto> listOf(List<Question> questions) {
        return questions
                .stream()
                .map(question -> new QuestionFindAllDto(
                        question.getQuestionNum(),
                        question.getQuestionTitle(),
                        question.getQuestionLevel()
                )).toList();
    }
}
