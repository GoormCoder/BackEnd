package goormcoder.webide.controller;

import goormcoder.webide.domain.ChatMessage;
import goormcoder.webide.dto.request.ChatMessageSendDto;
import goormcoder.webide.dto.request.ChatRoomCreateDto;
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
                "/pub/chats/room/" + savedMessage.getChatRoom().getId(),
                savedMessage.getMessage()
        );
    }

    @PostMapping("/chats/rooms")
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
    public ResponseEntity<String> createChatRoom(@Valid @RequestBody ChatRoomCreateDto chatRoomCreateDto) {
        chatRoomService.createChatRoom(principalHandler.getMemberLoginId(), chatRoomCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("채팅방이 생성되었습니다.");
    }

}
