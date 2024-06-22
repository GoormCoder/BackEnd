package goormcoder.webide.dto.response;

import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;

import java.time.LocalDateTime;

public record ChatRoomFindDto(

        Long chatRoomId,
        String chatRoomName,
        ChatMessageFindDto lastMessage,
        boolean hasUnreadMessages,
        LocalDateTime readAt

) {

    public static ChatRoomFindDto of(ChatRoom chatRoom, ChatRoomMember chatRoomMember,
                              ChatMessageFindDto chatMessageFindDto, boolean hasUnreadMessages) {
        return new ChatRoomFindDto(chatRoom.getId(), chatRoomMember.getChatRoomName(),
                chatMessageFindDto, hasUnreadMessages, chatRoomMember.getReadAt());
    }

}
