package goormcoder.webide.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import goormcoder.webide.domain.ChatRoom;

public record ChatRoomFindDto(

        Long chatRoomId,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String message

) {

    public static ChatRoomFindDto of(ChatRoom chatRoom) {
        return new ChatRoomFindDto(chatRoom.getId(), null);
    }

    public static ChatRoomFindDto of(ChatRoom chatRoom, String message) {
        return new ChatRoomFindDto(chatRoom.getId(), message);
    }

}
