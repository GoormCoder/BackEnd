package goormcoder.webide.dto.response;

import goormcoder.webide.domain.ChatRoom;

import java.util.List;

public record ChatRoomFindAllDto(

        Long chatRoomId,
        String chatRoomName,
        ChatMessageFindDto chatMessageFindDto

) {
}
