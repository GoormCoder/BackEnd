package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Board;

import java.time.LocalDateTime;
import java.util.List;

public record BoardFindAllDto(

        Long boarId,
        String title,
        LocalDateTime createdAt,
        int likeCount,
        MemberFindDto member,
        QuestionFindDto question
) {
    public static List<BoardFindAllDto> listOf(List<Board> boards) {
        return boards
                .stream()
                .map(board -> new BoardFindAllDto(
                        board.getId(),
                        board.getTitle(),
                        board.getCreatedAt(),
                        board.getLikeCount(),
                        MemberFindDto.from(board.getMember()),
                        QuestionFindDto.of(board.getQuestion())
                )).toList();
    }
}
