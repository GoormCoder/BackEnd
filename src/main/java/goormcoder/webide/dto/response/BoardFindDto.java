package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.enumeration.BoardType;

import java.time.LocalDateTime;

public record BoardFindDto(

        Long boarId,
        BoardType boardType,
        String title,
        String content,
        LocalDateTime createdAt,
        int LikeCount,
        MemberFindDto member,
        QuestionSummaryDto question
) {
    public static BoardFindDto from(Board board) {
        return new BoardFindDto(
                board.getId(),
                board.getBoardType(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedAt(),
                board.getLikeCount(),
                MemberFindDto.from(board.getMember()),
                QuestionSummaryDto.of(board.getQuestion())
        );
    }
}
