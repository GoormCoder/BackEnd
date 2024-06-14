package goormcoder.webide.exception;

import goormcoder.webide.common.dto.ErrorMessage;
import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException {

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
