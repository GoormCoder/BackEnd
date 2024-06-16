package goormcoder.webide.exception;

import goormcoder.webide.common.dto.ErrorMessage;
import lombok.Getter;

@Getter
public class ForbiddenException extends BusinessException {

    public ForbiddenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
