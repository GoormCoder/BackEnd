package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.ChatRoomCreateDto;
import goormcoder.webide.repository.ChatRoomRepository;
import goormcoder.webide.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void createChatRoom(String loginId, ChatRoomCreateDto chatRoomCreateDto) {
        Member owner = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
        Member guest = memberRepository.findByLoginId(chatRoomCreateDto.invitedMemberLoginId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));

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
    }

}
