package goormcoder.webide.dto.response;

public record ChatRoomFindDto(

        Long chatRoomId,
        String chatRoomName,
        ChatMessageFindDto lastMessage

) {
}
