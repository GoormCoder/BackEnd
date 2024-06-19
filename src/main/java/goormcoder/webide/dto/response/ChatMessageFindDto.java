package goormcoder.webide.dto.response;

import goormcoder.webide.domain.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageFindDto(

        String message,
        LocalDateTime createdAt

) {

    public static ChatMessageFindDto from(ChatMessage chatMessage) {
        return new ChatMessageFindDto(
                chatMessage.getMessage(),
                chatMessage.getCreatedAt()
        );
    }

}
