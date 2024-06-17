package goormcoder.webide.exception;

import goormcoder.webide.constants.ErrorMessages;

public class BusinessException extends RuntimeException {
    private ErrorMessages errorMessagess;
    public BusinessException(ErrorMessages errorMessagess) {
        super(errorMessagess.getMessage());
        this.errorMessagess = errorMessagess;
    }
}
