package goormcoder.webide.controller;

import goormcoder.webide.dto.request.ChatRoomCreateDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Chat", description = "1:1 채팅 관련 API")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final PrincipalHandler principalHandler;

    @PostMapping("/chats/rooms")
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
    public ResponseEntity<String> createChatRoom(@Valid @RequestBody ChatRoomCreateDto chatRoomCreateDto) {
        chatRoomService.createChatRoom(principalHandler.getMemberLoginId(), chatRoomCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("채팅방이 생성되었습니다.");
    }

}
