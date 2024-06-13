package goormcoder.webide.dto.request;

public record CommentCreateDto(

        Long memberId,
        String content
) {
}
