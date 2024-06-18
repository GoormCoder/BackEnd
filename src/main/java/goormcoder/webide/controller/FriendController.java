package goormcoder.webide.controller;

import goormcoder.webide.dto.request.FriendCreateDto;
import goormcoder.webide.dto.request.FriendRequestCreatDto;
import goormcoder.webide.dto.response.FriendFindAllDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Friend", description = "친구 관련 API")
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;
    private final PrincipalHandler principalHandler;

    // 친구 요청
    @PostMapping("/request/{loginId}")
    @Operation(summary = "친구요청")
    public ResponseEntity<String> requestFriend(@PathVariable String loginId, @Valid @RequestBody FriendRequestCreatDto friendRequestCreateDto) {
        if(friendService.requestFriend(loginId, friendRequestCreateDto)){
            return ResponseEntity.status(HttpStatus.OK).body("친구요청이 완료되었습니다");
        } //중복 체크 할것
        return ResponseEntity.status(HttpStatus.OK).body("이미 요청한 멤버입니다");
    }

    //친구 추가
    @PostMapping("/add/{loginId}")
    @Operation(summary = "친구추가(수락)")
    public ResponseEntity<String> createFriend(@PathVariable String loginId, @Valid @RequestBody FriendCreateDto friendCreateDto) {
        friendService.createFriend(loginId, friendCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("친구추가가 완료되었습니다.");
    }

    //친구 조회
//    @GetMapping("/all")
    @GetMapping("/{loginId}")
    @Operation(summary = "친구 조회", description = "로그인한 사용자의 전체 친구를 조회합니다.")
    public ResponseEntity<List<FriendFindAllDto>> getAllFriends(@PathVariable String loginId) {
//        Long loginId = principalHandler.getMemberLoginId();
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getAllFriends(loginId));
    }
}
