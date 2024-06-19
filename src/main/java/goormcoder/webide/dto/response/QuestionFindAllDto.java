package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;

import java.util.List;
import java.util.Random;

public record QuestionFindAllDto(
        long id,
        String state,
        String title,
        int level,
        int solved,
        int accuracy
) {

        public static List<QuestionFindAllDto> listOf(List<Question> questions) {
            // state, solved, accuracy 는 추후에 로직 추가
            String[] options = {"F", "T", null};
            Random random = new Random();
        return questions
                .stream()
                .map(question -> new QuestionFindAllDto(
                        question.getId(),
                        options[random.nextInt(options.length)],
                        question.getTitle(),
                        question.getLevel(),
                        random.nextInt(1000),
                        random.nextInt(100)
                )).toList();
    }
}
