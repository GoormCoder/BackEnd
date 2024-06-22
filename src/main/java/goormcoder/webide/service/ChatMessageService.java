package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.ChatMessage;
import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.ChatMessageSendDto;
import goormcoder.webide.dto.response.ChatMessageFindDto;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.ChatMessageRepository;
import goormcoder.webide.repository.ChatRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final MemberService memberService;
    private final ChatRoomMemberService chatRoomMemberService;

    @Transactional
    public ChatMessage saveMessage(ChatMessageSendDto chatMessageSendDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageSendDto.chatRoomId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.CHATROOM_NOT_FOUND.getMessage()));
        Member sender = memberService.findByLoginId(chatMessageSendDto.senderLoginId());

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

        LocalDateTime rejoinedAt = chatRoomMember.getReJoinedAt();
        List<ChatMessage> messages = chatMessageRepository.findMessageByChatRoomId(chatRoomId, rejoinedAt);
        return ChatMessageFindDto.listOf(messages);
    }

    @Transactional
    public void deleteChatMessage(Long chatRoomId, Long chatMessageId, String loginId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.CHATROOM_NOT_FOUND.getMessage()));
        chatRoomMemberService.checkChatRoomMember(chatRoom, loginId);

        ChatMessage chatMessage = checkChatMessage(chatRoomId, chatMessageId, loginId);
        chatMessage.updateMessage();
        chatRoomRepository.save(chatRoom);
    }

    private void updateReadAt(ChatRoom chatRoom, Member sender, ChatMessage chatMessage) {
        ChatRoomMember chatRoomMember = chatRoomMemberService.findByChatRoomAndMember(chatRoom, sender);
        chatRoomMember.markAsRead(chatMessage.getCreatedAt());
    }

    public ChatMessage getLastMessage(Long chatRoomId, ChatRoomMember chatRoomMember) {
        LocalDateTime rejoinedAt = chatRoomMember.getReJoinedAt();
        return chatMessageRepository.findLastMessageByChatRoomId(chatRoomId, rejoinedAt)
                .stream()
                .findFirst()
                .orElse(null);
    }

    private ChatMessage checkChatMessage(Long chatRoomId, Long chatMessageId, String loginId) {
        ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.CHAT_MESSAGE_NOT_FOUND.getMessage()));

        if(!chatMessage.getChatRoom().getId().equals(chatRoomId)) {
            throw new EntityNotFoundException(ErrorMessages.CHAT_MESSAGE_NOT_FOUND.getMessage());
        }

        if(!chatMessage.getMember().getLoginId().equals(loginId)) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_MESSAGE_ACCESS);
        }

        return chatMessage;
    }

}
