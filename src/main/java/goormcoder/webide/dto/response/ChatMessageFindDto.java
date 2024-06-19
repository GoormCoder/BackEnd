package goormcoder.webide.dto.response;

import java.time.LocalDateTime;

public record ChatMessageFindDto(

        String message,
        LocalDateTime createdAt,
        MessageSenderFindDto sender

) {

}
