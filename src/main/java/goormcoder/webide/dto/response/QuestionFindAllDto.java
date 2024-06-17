package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;

import java.util.List;

public record QuestionFindAllDto(
        long id,
        String title,
        int level,
        int solved,
        int accuracy
) {
        // solved 및 accuracy 는 추후에 로직 추가
        public static List<QuestionFindAllDto> listOf(List<Question> questions) {
        return questions
                .stream()
                .map(question -> new QuestionFindAllDto(
                        question.getId(),
                        question.getTitle(),
                        question.getLevel(),
                        1000,
                        90
                )).toList();
    }
}
