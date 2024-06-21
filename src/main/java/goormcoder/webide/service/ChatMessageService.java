package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.ChatMessage;
import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.ChatMessageSendDto;
import goormcoder.webide.dto.response.ChatMessageFindDto;
import goormcoder.webide.repository.ChatMessageRepository;
import goormcoder.webide.repository.ChatRoomMemberRepository;
import goormcoder.webide.repository.ChatRoomRepository;
import goormcoder.webide.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomMemberService chatRoomMemberService;

    @Transactional
    public ChatMessage saveMessage(ChatMessageSendDto chatMessageSendDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageSendDto.chatRoomId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.CHATROOM_NOT_FOUND.getMessage()));
        Member sender = memberRepository.findByLoginId(chatMessageSendDto.senderLoginId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));

        ChatMessage chatMessage = ChatMessage.of(chatMessageSendDto.message(), sender, chatRoom);
        chatRoom.addChatMessage(chatMessage);

        updateReadAt(chatRoom, sender, chatMessage);
        return chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public List<ChatMessageFindDto> getChatRoomMessages(Long chatRoomId, String loginId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.CHATROOM_NOT_FOUND.getMessage()));
        ChatRoomMember chatRoomMember = chatRoomMemberService.checkChatRoomMember(chatRoom, loginId);
        chatRoomMember.markAsRead(LocalDateTime.now());
        return ChatMessageFindDto.listOf(chatMessageRepository.findMessageByChatRoomId(chatRoomId));
    }

    private void updateReadAt(ChatRoom chatRoom, Member sender, ChatMessage chatMessage) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, sender);
        chatRoomMember.markAsRead(chatMessage.getCreatedAt());
    }

    public ChatMessage getLastMessage(Long chatRoomId) {
        return chatMessageRepository.findLastMessageByChatRoomId(chatRoomId)
                .stream()
                .findFirst()
                .orElse(null);
    }

}
