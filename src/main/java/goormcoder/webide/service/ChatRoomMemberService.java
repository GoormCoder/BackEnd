package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;
import goormcoder.webide.exception.ForbiddenException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberService {

    public void checkChatRoomMember(ChatRoom chatRoom, String loginId) {
        Optional<ChatRoomMember> roomMember = chatRoom.getChatRoomMembers()
                .stream()
                .filter(member -> member.getMember().getLoginId().equals(loginId))
                .findFirst();

        boolean isMember = roomMember.isPresent();

        boolean isDeleted = roomMember
                .map(member -> member.getDeletedAt() != null)
                .orElse(false);

        if(!isMember) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_CHATROOM_ACCESS);
        } else if(isDeleted) {
            throw new EntityNotFoundException(ErrorMessages.CHATROOM_NOT_FOUND.getMessage());
        }
    }

}
