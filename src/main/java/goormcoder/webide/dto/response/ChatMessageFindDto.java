package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageFindDto(

        String message,
        LocalDateTime createdAt,
        MessageSenderFindDto sender

) {

    public static List<ChatMessageFindDto> listOf(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(chatMessage -> new ChatMessageFindDto(
                        chatMessage.getMessage(),
                        chatMessage.getCreatedAt(),
                        MessageSenderFindDto.from(chatMessage.getMember())
                )).toList();
    }

}
