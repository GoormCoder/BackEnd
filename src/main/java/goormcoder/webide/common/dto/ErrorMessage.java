package goormcoder.webide.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    MEMBER_NOT_FOUND("해당하는 사용자가 존재하지 않습니다."),
    QUESTION_NOT_FOUND("해당하는 문제가 존재하지 않습니다."),
    ;

    private final String message;
}
