package goormcoder.webide.dto.request;

public record QuestionUpdateDto(
        String title,
        int level,
        String content
) {

}
