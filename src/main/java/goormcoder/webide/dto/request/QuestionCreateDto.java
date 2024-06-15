package goormcoder.webide.dto.request;

public record QuestionCreateDto (
        String title,
        int level,
        String content
) {

}
