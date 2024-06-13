package goormcoder.webide.exception;

import goormcoder.webide.common.dto.ErrorMessage;

public class BusinessException extends RuntimeException {
    private ErrorMessage errorMessage;
    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
