package goormcoder.webide.dto.request;

import goormcoder.webide.constants.ChatConstants;
import jakarta.validation.constraints.NotNull;

public record ChatMessageSendDto(

        @NotNull(message = ChatConstants.CHATROOM_ID_IS_NULL)
        Long chatRoomId,

        @NotNull(message = ChatConstants.MESSAGE_IS_NULL)
        String message

) {
}
