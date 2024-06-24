package goormcoder.webide.controller;

import goormcoder.webide.dto.request.MyPageDto;
import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
// import org.hibernate.mapping.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@Tag(name = "MyPage", description = "마이페이지 관련 API")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/{loginId}")
    @Operation(summary = "마이페이지 조회", description = "로그인 아이디에 해당하는 마이페이지 정보를 조회합니다.")
    public ResponseEntity<?> getMyPage(@PathVariable String loginId) {
        try {
            MyPageDto myPageDto = myPageService.getMyPageByLoginId(loginId);
            return ResponseEntity.status(HttpStatus.OK).body(myPageDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage("해당 로그인 아이디를 가진 사용자를 찾을 수 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
        }
    }

    @PatchMapping("/{loginId}/nick")
    @Operation(summary = "닉네임 수정", description = "로그인 아이디에 해당하는 닉네임을 수정합니다.")
    public ResponseEntity<?> updateNick(@PathVariable String loginId, @RequestBody Map<String, String> request) {
        try {
            String newNick = request.get("newNick");
            myPageService.updateNick(loginId, newNick);
            return ResponseEntity.status(HttpStatus.OK).body("닉네임이 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage("해당 로그인 아이디를 가진 사용자를 찾을 수 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
        }
    }

    @PatchMapping("/{loginId}/info")
    @Operation(summary = "정보 수정", description = "로그인 아이디에 해당하는 정보를 수정합니다.")
    public ResponseEntity<?> updateInfo(@PathVariable String loginId, @RequestBody Map<String, String> request) {
        try {
            String newInfo = request.get("newInfo");
            myPageService.updateInfo(loginId, newInfo);
            return ResponseEntity.status(HttpStatus.OK).body("정보가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage("해당 로그인 아이디를 가진 사용자를 찾을 수 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
        }
    }

    @PatchMapping("/{loginId}/birth")
    @Operation(summary = "생일 수정", description = "로그인 아이디에 해당하는 생일을 수정합니다.")
    public ResponseEntity<?> updateBirth(@PathVariable String loginId, @RequestBody LocalDate newBirth) {
        try {
            myPageService.updateBirth(loginId, newBirth);
            return ResponseEntity.status(HttpStatus.OK).body("생일이 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage("해당 로그인 아이디를 가진 사용자를 찾을 수 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
        }
    }

    

    static class ErrorMessage {
        private String message;

        public ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}