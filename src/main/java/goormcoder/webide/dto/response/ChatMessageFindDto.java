package goormcoder.webide.dto.response;

import goormcoder.webide.domain.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageFindDto(

        Long messageId,
        String message,
        LocalDateTime createdAt,
        MessageSenderFindDto sender

) {

    public static List<ChatMessageFindDto> listOf(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(ChatMessageFindDto::of)
                .toList();
    }

    public static ChatMessageFindDto of(ChatMessage chatMessage) {
        if(chatMessage == null) {
            return null;
        }
        
        return new ChatMessageFindDto(
                chatMessage.getId(),
                chatMessage.getMessage(),
                chatMessage.getCreatedAt(),
                MessageSenderFindDto.from(chatMessage.getMember())
        );
    }

}
