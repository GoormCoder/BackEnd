package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.enumeration.BoardType;

import java.time.LocalDateTime;
import java.util.List;

public record BoardFindAllDto(

        Long boarId,
        BoardType boardType,
        String title,
        LocalDateTime createdAt,
        int likeCount,
        MemberFindDto member,
        QuestionSummaryDto question
) {
    public static List<BoardFindAllDto> listOf(List<Board> boards) {
        return boards
                .stream()
                .map(board -> new BoardFindAllDto(
                        board.getId(),
                        board.getBoardType(),
                        board.getTitle(),
                        board.getCreatedAt(),
                        board.getLikeCount(),
                        MemberFindDto.from(board.getMember()),
                        QuestionSummaryDto.of(board.getQuestion())
                )).toList();
    }
}
