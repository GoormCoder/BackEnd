package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberService {

    public void checkChatRoomMember(ChatRoom chatRoom, String loginId) {
        boolean isMember = chatRoom.getChatRoomMembers()
                .stream()
                .anyMatch(member -> member.getMember().getLoginId().equals(loginId));

        if (!isMember) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_CHATROOM_ACCESS);
        }
    }

}
