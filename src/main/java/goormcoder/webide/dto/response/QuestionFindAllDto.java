package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;

import java.util.List;

public record QuestionFindAllDto(
        long questionNum,
        String questionTitle,
        int questionLevel,
        int questionSolved,
        int questionAccuracy
) {
        // solved 및 accuracy 는 추후에 로직 추가
        public static List<QuestionFindAllDto> listOf(List<Question> questions) {
        return questions
                .stream()
                .map(question -> new QuestionFindAllDto(
                        question.getQuestionNum(),
                        question.getQuestionTitle(),
                        question.getQuestionLevel(),
                        1000,
                        90
                )).toList();
    }
}
