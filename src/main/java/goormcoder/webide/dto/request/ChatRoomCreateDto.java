package goormcoder.webide.dto.request;

import goormcoder.webide.constants.ChatConstants;
import jakarta.validation.constraints.NotNull;

public record ChatRoomCreateDto(

        String chatRoomName,

        @NotNull(message = ChatConstants.INVITED_MEMBER_ID_IS_NULL)
        String invitedMemberLoginId

) {
}
