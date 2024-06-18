package goormcoder.webide.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SolveResult {

    CORRECT("정답"),
    WRONG("오답"),
    TIME_OVER("시간 초과"),
    OUT_OF_MEMORY("메모리 초과"),
    NOT_TRIED_YET("시도하지 않음"),
    ;

    private String message;

}
