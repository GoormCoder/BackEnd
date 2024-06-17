package goormcoder.webide.exception;

import goormcoder.webide.constants.ErrorMessages;
import lombok.Getter;

@Getter
public class ConflictException extends BusinessException {

    public ConflictException(ErrorMessages errorMessages) {
        super(errorMessages);
    }
}
