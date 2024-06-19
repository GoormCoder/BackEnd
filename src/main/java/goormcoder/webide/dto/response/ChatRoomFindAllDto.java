package goormcoder.webide.dto.response;

public record ChatRoomFindAllDto(

        Long chatRoomId,
        String chatRoomName,
        ChatMessageFindDto lastMessage

) {
}
