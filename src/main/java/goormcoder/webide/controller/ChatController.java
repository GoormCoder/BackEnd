package goormcoder.webide.controller;

import goormcoder.webide.domain.ChatMessage;
import goormcoder.webide.dto.request.ChatMessageSendDto;
import goormcoder.webide.dto.request.ChatRoomCreateDto;
import goormcoder.webide.dto.response.ChatMessageFindDto;
import goormcoder.webide.dto.response.ChatRoomFindDto;
import goormcoder.webide.dto.response.ChatRoomInfoDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.ChatMessageService;
import goormcoder.webide.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Chat", description = "1:1 채팅 관련 API")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    private final PrincipalHandler principalHandler;
    private final SimpMessageSendingOperations messagingTemplate;

    // Web Socket Connection URL - ws://localhost:8080/ws
    @MessageMapping("/send")
    public void sendMessage(@Payload ChatMessageSendDto chatMessageSendDto) {
        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessageSendDto);
        messagingTemplate.convertAndSend(
                "/sub/chats/room/" + savedMessage.getChatRoom().getId(),
                ChatMessageFindDto.of(savedMessage)
        );
    }

    @PostMapping("/chats/rooms")
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
    public ResponseEntity<ChatRoomInfoDto> createChatRoom(@Valid @RequestBody ChatRoomCreateDto chatRoomCreateDto) {
        ChatRoomInfoDto chatRoomInfoDto = chatRoomService.createChatRoom(principalHandler.getMemberLoginId(), chatRoomCreateDto);

        if(chatRoomInfoDto.message() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(chatRoomInfoDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(chatRoomInfoDto);
    }

    @GetMapping("/chats/rooms")
    @Operation(summary = "채팅방 조회", description = "사용자가 참여하고 있는 전체 채팅방을 조회합니다.")
    public ResponseEntity<List<ChatRoomFindDto>> getMyChatRooms() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(chatRoomService.getMyChatRooms(principalHandler.getMemberLoginId()));
    }

    @DeleteMapping("/chats/rooms/{chatRoomId}")
    @Operation(summary = "채팅방 삭제", description = "사용자의 특정 채팅방을 삭제합니다.")
    public ResponseEntity<String> deleteMyChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteMyChatRoom(chatRoomId, principalHandler.getMemberLoginId());
        return ResponseEntity.status(HttpStatus.OK).body("채팅방이 삭제되었습니다.");
    }

    @GetMapping("/chats/rooms/{chatRoomId}")
    @Operation(summary = "메시지 조회", description = "특정 채팅방의 메시지를 조회합니다. 메시지는 생성일 기준 오름차순 정렬되어있습니다.")
    public ResponseEntity<List<ChatMessageFindDto>> getChatRoomMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(chatMessageService.getChatRoomMessages(chatRoomId, principalHandler.getMemberLoginId()));
    }

    @DeleteMapping("/chats/rooms/{chatRoomId}/{chatMessageId}")
    @Operation(summary = "메시지 삭제", description = "메시지를 삭제합니다.")
    public ResponseEntity<String> deleteChatMessage(@PathVariable Long chatRoomId, @PathVariable Long chatMessageId) {
        chatMessageService.deleteChatMessage(chatRoomId, chatMessageId, principalHandler.getMemberLoginId());
        return ResponseEntity.status(HttpStatus.OK).body("메시지가 삭제되었습니다.");
    }

}
