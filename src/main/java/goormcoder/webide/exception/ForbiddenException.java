package goormcoder.webide.exception;

import goormcoder.webide.constants.ErrorMessages;
import lombok.Getter;

@Getter
public class ForbiddenException extends BusinessException {

    public ForbiddenException(ErrorMessages errorMessagess) {
        super(errorMessagess);
    }
}
