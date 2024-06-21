package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.ChatRoomCreateDto;
import goormcoder.webide.dto.response.ChatMessageFindDto;
import goormcoder.webide.dto.response.ChatRoomFindDto;
import goormcoder.webide.dto.response.ChatRoomInfoDto;
import goormcoder.webide.dto.response.MessageSenderFindDto;
import goormcoder.webide.repository.ChatRoomRepository;
import goormcoder.webide.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final MemberService memberService;

    @Transactional
    public ChatRoomInfoDto createChatRoom(String loginId, ChatRoomCreateDto chatRoomCreateDto) {
        Member owner = memberService.findByLoginId(loginId);
        Member guest = memberService.findByLoginId(chatRoomCreateDto.invitedMemberLoginId());

        // 로그인한 사용자와 초대된 사용자가 같은 경우
        validateSameUser(owner, guest);

        // 채팅방 중복 개설 확인 및 재입장 처리
        String uniqueKey = generateUniqueKey(owner, guest);
        if(chatRoomRepository.existsByUniqueKey(uniqueKey)) {
            return handleExistingChatRoom(uniqueKey, owner);
        }

        ChatRoom chatRoom = ChatRoom.of(uniqueKey);
        chatRoom.addChatRoomMember(ChatRoomMember.of(guest.getName(), owner, chatRoom));
        chatRoom.addChatRoomMember(ChatRoomMember.of(owner.getName(), guest, chatRoom));

        chatRoomRepository.save(chatRoom);

        return ChatRoomInfoDto.of(chatRoom);
    }

    @Transactional
    public List<ChatRoomFindDto> getMyChatRooms(String loginId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByMemberLoginId(loginId);

        return chatRooms.stream()
                .map(chatRoom -> {
                    ChatMessageFindDto lastMessageDto = chatMessageService.getLastMessage(chatRoom.getId())
                            .map(lastMessage -> new ChatMessageFindDto(
                                    lastMessage.getMessage(),
                                    lastMessage.getCreatedAt(),
                                    MessageSenderFindDto.from(lastMessage.getMember()))
                            ).orElse(null);

                    String chatRoomName = findChatRoomMember(chatRoom, memberService.findByLoginId(loginId))
                            .map(ChatRoomMember::getChatRoomName)
                            .orElse("알수없음");

                    return new ChatRoomFindDto(chatRoom.getId(), chatRoomName, lastMessageDto);
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

    private void validateSameUser(Member owner, Member guest) {
        if(owner == guest) {
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST_INVITED_ID.getMessage());
        }
    }

    private String generateUniqueKey(Member owner, Member guest) {
        return Stream.of(owner.getLoginId(), guest.getLoginId())
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(""));
    }

    private Optional<ChatRoomMember> findChatRoomMember(ChatRoom chatRoom, Member member) {
        return chatRoom.getChatRoomMembers().stream()
                .filter(roomMember -> roomMember.getMember().equals(member))
                .findFirst();
    }

    private ChatRoomInfoDto handleExistingChatRoom(String uniqueKey, Member owner) {
        ChatRoom existingChatRoom = chatRoomRepository.findByUniqueKey(uniqueKey);

        Optional<ChatRoomMember> ownerMember = findChatRoomMember(existingChatRoom, owner);
        if(ownerMember.isPresent()) {
            boolean ownerDeleted = ownerMember.get().isDeleted();

            if(ownerDeleted) {
                ownerMember.get().markAsReJoined();
                chatRoomRepository.save(existingChatRoom);
                return ChatRoomInfoDto.of(existingChatRoom);
            }
        }

        return ChatRoomInfoDto.of(existingChatRoom, ErrorMessages.CHATROOM_CONFLICT.getMessage());
    }

}
