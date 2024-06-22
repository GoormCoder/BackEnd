package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;
import goormcoder.webide.domain.Member;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.ChatRoomMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatRoomMember findByChatRoomAndMember(ChatRoom chatRoom, Member sender) {
        return chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, sender);
    }

    public ChatRoomMember checkChatRoomMember(ChatRoom chatRoom, String loginId) {
        ChatRoomMember roomMember = chatRoom.getChatRoomMembers()
                .stream()
                .filter(member -> member.getMember().getLoginId().equals(loginId))
                .findFirst()
                .orElseThrow(() -> new ForbiddenException(ErrorMessages.FORBIDDEN_CHATROOM_ACCESS));

        if(roomMember.isDeleted()) {
            throw new EntityNotFoundException(ErrorMessages.CHATROOM_NOT_FOUND.getMessage());
        }

        return roomMember;
    }

}
