package goormcoder.webide.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler
    @SendTo("/pub/queue/errors")
    public String handleException(Exception exception) {
        return exception.getMessage();
    }

}
