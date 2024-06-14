package goormcoder.webide.exception;

import goormcoder.webide.common.dto.ErrorMessage;
import lombok.Getter;

@Getter
public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
