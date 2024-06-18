package goormcoder.webide.controller;

import goormcoder.webide.dto.request.CommentCreateDto;
import goormcoder.webide.dto.request.FriendCreateDto;
import goormcoder.webide.dto.response.FriendFindAllDto;
import goormcoder.webide.dto.response.QuestionFindAllDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.FriendService;
import goormcoder.webide.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;
    private final PrincipalHandler principalHandler;

    //댓글 생성
    @PostMapping("/add/{loginId}")
    @Operation(summary = "친구 추가", description = "친구를 추가합니다.")
    public ResponseEntity<?> createFriend(@PathVariable String loginId, @Valid @RequestBody FriendCreateDto friendCreateDto) {
        if(friendService.createFriend(loginId, friendCreateDto)){
            return ResponseEntity.status(HttpStatus.OK).body("친구추가가 완료되었습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("이미 추가된 친구입니다.");
    }

    @GetMapping("/{loginId}")
    @Operation(summary = "친구 조회", description = "로그인한 사용자의 전체 친구를 조회합니다.")
    public ResponseEntity<List<FriendFindAllDto>> getAllFriends(@PathVariable String loginId) {
//        Long loginId = principalHandler.getMemberLoginId();
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getAllFriends(loginId));
    }
}
