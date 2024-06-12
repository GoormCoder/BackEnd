package goormcoder.webide.domain.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BoardType {

    FREE_BOARD,
    QUESTION_BOARD
}
