package goormcoder.webide.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    //404 NOT_FOUND
    MEMBER_NOT_FOUND("해당하는 사용자가 존재하지 않습니다."),
    QUESTION_NOT_FOUND("해당하는 문제가 존재하지 않습니다."),
    BOARD_NOT_FOUND("해당하는 게시글이 존재하지 않습니다."),
    COMMENT_NOT_FOUND("해당하는 댓글이 존재하지 않습니다."),
    LIKE_NOT_FOUND("해당하는 좋아요가 존재하지 않습니다."),

    //409 CONFLICT
    LIKE_CONFLICT("이미 좋아요를 누르셨습니다."),
    ;

    private final String message;
}
