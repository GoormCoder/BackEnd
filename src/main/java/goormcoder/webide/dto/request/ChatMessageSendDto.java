package goormcoder.webide.dto.request;

import goormcoder.webide.constants.ChatConstants;
import jakarta.validation.constraints.NotNull;

public record ChatMessageSendDto(

        @NotNull(message = ChatConstants.CHATROOM_ID_IS_NULL)
        Long chatRoomId,

        // Web Socket 테스트용 필드 - 추후 삭제 예정
        String senderLoginId,

        @NotNull(message = ChatConstants.MESSAGE_IS_NULL)
        String message

) {
}
