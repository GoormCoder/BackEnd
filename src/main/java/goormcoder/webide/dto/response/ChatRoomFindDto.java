package goormcoder.webide.dto.response;

import goormcoder.webide.domain.ChatRoom;

public record ChatRoomFindDto(

        Long chatRoomId

) {

    public static ChatRoomFindDto of(ChatRoom chatRoom) {
        return new ChatRoomFindDto(chatRoom.getId());
    }

}
