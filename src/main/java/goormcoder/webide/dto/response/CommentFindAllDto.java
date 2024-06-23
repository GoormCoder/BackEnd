package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record CommentFindAllDto(

        Long commentId,
        String comment,
        LocalDateTime createdAt,
        MemberFindDto member
) {
    public static List<CommentFindAllDto> listOf(List<Comment> comments) {
        return comments
                .stream()
                .map(comment -> new CommentFindAllDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        MemberFindDto.from(comment.getMember())
                )).toList();
    }
    public static CommentFindAllDto of(Comment comment) {
        return new CommentFindAllDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        MemberFindDto.from(comment.getMember())
                );
    }
}
