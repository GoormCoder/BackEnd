package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.enumeration.BoardType;

import java.time.LocalDateTime;

public record BoardFindDto(

        BoardType boardType,
        String title,
        String content,
        LocalDateTime createdAt,
        MemberFindDto member,
        QuestionFindDto question
) {
    public static BoardFindDto from(Board board) {
        return new BoardFindDto(
                board.getBoardType(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedAt(),
                MemberFindDto.from(board.getMember()),
                QuestionFindDto.of(board.getQuestion())
        );
    }
}
