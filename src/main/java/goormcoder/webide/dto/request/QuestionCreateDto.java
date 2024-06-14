package goormcoder.webide.dto.request;

public record QuestionCreateDto (
        String title,
        String content,
        int level
) {

}
