package goormcoder.webide.exception;

import goormcoder.webide.common.dto.ErrorMessage;
import lombok.Getter;

@Getter
public class ConflictException extends BusinessException {

    public ConflictException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
