package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;
import java.util.List;
import java.util.Random;

public record QuestionSummaryDto(
        Long id,
        Integer level,
        String title,
        List<QuestionTagSummaryDto> tags,
        String state,
        int solved,
        int accuracy
) {
    public static QuestionSummaryDto of(Question question) {
        // state, solved, accuracy 는 추후에 로직 추가
        String[] options = {"F", "T", null};
        Random random = new Random();

        if (question == null) {
            return null;
        }
        return new QuestionSummaryDto(
                question.getId(),
                question.getLevel(),
                question.getTitle(),
                QuestionTagSummaryDto.listOf(question.getTags()),
                options[random.nextInt(options.length)],
                random.nextInt(1000),
                random.nextInt(100)
        );
    }
    public static List<QuestionSummaryDto> listOf(List<Question> questions) {
        return questions.stream()
                .map(QuestionSummaryDto::of)
                .toList();
    }
}
