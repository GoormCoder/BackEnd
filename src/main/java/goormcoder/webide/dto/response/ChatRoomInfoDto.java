package goormcoder.webide.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import goormcoder.webide.domain.ChatRoom;

public record ChatRoomInfoDto(

        Long chatRoomId,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String message

) {

    public static ChatRoomInfoDto of(ChatRoom chatRoom) {
        return new ChatRoomInfoDto(chatRoom.getId(), null);
    }

    public static ChatRoomInfoDto of(ChatRoom chatRoom, String message) {
        return new ChatRoomInfoDto(chatRoom.getId(), message);
    }

}
