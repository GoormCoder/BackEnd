package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.enumeration.BoardType;
import java.time.LocalDateTime;

public record BoardSummaryDto(

        Long boarId,
        BoardType boardType,
        String title,
        LocalDateTime createdAt,
        int LikeCount,
        MemberFindDto member,
        QuestionSummaryDto question
) {
    public static BoardSummaryDto of(Board board) {
        return new BoardSummaryDto(
                board.getId(),
                board.getBoardType(),
                board.getTitle(),
                board.getCreatedAt(),
                board.getLikeCount(),
                MemberFindDto.from(board.getMember()),
                QuestionSummaryDto.of(board.getQuestion())
        );
    }
}
