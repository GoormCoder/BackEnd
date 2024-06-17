package goormcoder.webide.exception;

import goormcoder.webide.constants.ErrorMessages;

public class BusinessException extends RuntimeException {
    private ErrorMessages errorMessages;
    public BusinessException(ErrorMessages errorMessages) {
        super(errorMessages.getMessage());
        this.errorMessages = errorMessages;
    }
}
