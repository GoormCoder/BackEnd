package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.ChatRoomCreateDto;
import goormcoder.webide.dto.response.ChatMessageFindDto;
import goormcoder.webide.dto.response.ChatRoomFindAllDto;
import goormcoder.webide.dto.response.ChatRoomFindDto;
import goormcoder.webide.dto.response.MessageSenderFindDto;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.ChatRoomRepository;
import goormcoder.webide.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomMemberService chatRoomMemberService;

    @Transactional
    public ChatRoomFindDto createChatRoom(String loginId, ChatRoomCreateDto chatRoomCreateDto) {
        Member owner = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
        Member guest = memberRepository.findByLoginId(chatRoomCreateDto.invitedMemberLoginId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));

        // 로그인한 사용자와 초대된 사용자가 같은 경우
        if(owner == guest) {
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST_INVITED_ID.getMessage());
        }

        // 해당 사용자와의 채팅방이 이미 개설되어 있는 경우
        String uniqueKey = Stream.of(owner.getLoginId(), guest.getLoginId())
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(""));

        if(chatRoomRepository.existsByUniqueKey(uniqueKey)) {
            throw new DataIntegrityViolationException(ErrorMessages.CHATROOM_CONFLICT.getMessage());
        }

        ChatRoom chatRoom = ChatRoom.of(chatRoomCreateDto.chatRoomName(), uniqueKey);
        chatRoom.addChatRoomMember(ChatRoomMember.of(owner, chatRoom));
        chatRoom.addChatRoomMember(ChatRoomMember.of(guest, chatRoom));

        chatRoomRepository.save(chatRoom);

        return ChatRoomFindDto.of(chatRoom);
    }

    @Transactional
    public List<ChatRoomFindAllDto> getMyChatRooms(String loginId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByMemberLoginId(loginId);

        return chatRooms.stream()
                .map(chatRoom -> {
                    // 채팅방 이름이 없는 경우 상대방 아이디로 지정
                    String chatRoomName = chatRoom.getChatRoomName();
                    if(chatRoomName == null || chatRoomName.trim().isEmpty()) {
                        chatRoomName = chatRoomRepository.findChatRoomOtherMemberUsername(chatRoom.getId(), loginId);
                    }
                    ChatMessageFindDto lastMessageDto = chatMessageService.getLastMessage(chatRoom.getId())
                            .map(lastMessage -> new ChatMessageFindDto(
                                    lastMessage.getMessage(),
                                    lastMessage.getCreatedAt(),
                                    MessageSenderFindDto.from(lastMessage.getMember()))
                            ).orElse(null);
                    return new ChatRoomFindAllDto(chatRoom.getId(), chatRoomName, lastMessageDto);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMyChatRoom(Long chatRoomId, String loginId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.CHATROOM_NOT_FOUND.getMessage()));

        ChatRoomMember chatRoomMember = chatRoomMemberService.checkChatRoomMember(chatRoom, loginId);

        chatRoomMember.markAsDeleted();
        chatRoomRepository.save(chatRoom);

        deleteChatRoom(chatRoom);
    }

    private void deleteChatRoom(ChatRoom chatRoom) {
        boolean allMembersDeleted = chatRoom.getChatRoomMembers().stream()
                .allMatch(ChatRoomMember::isDeleted);

        if(allMembersDeleted) {
            chatRoom.markAsDeleted();
            chatRoom.deleteUniqueKey();
            chatRoomRepository.save(chatRoom);
        }
    }

}
