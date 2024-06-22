package goormcoder.webide.controller;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.FindLoginIdDto;
import goormcoder.webide.dto.request.FindPasswordDto;
import goormcoder.webide.dto.request.MemberJoinDto;
import goormcoder.webide.dto.request.ResetPasswordDto;
import goormcoder.webide.dto.request.MemberLoginDto;
import goormcoder.webide.dto.request.RefreshTokenDto;
import goormcoder.webide.dto.response.JwtTokenDto;
import goormcoder.webide.dto.response.MemberFindAllDto;
import goormcoder.webide.service.AuthService;
import goormcoder.webide.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@Tag(name = "Member", description = "회원 관련 API")
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 성공 시 JWT을 발급합니다.")
    public ResponseEntity<?> login(@Valid @RequestBody MemberLoginDto loginDto) {
        try {
            JwtTokenDto tokenDto = authService.authenticate(loginDto);
            return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(ErrorMessages.JWT_BAD_CREDENTIAL_EXCEPTION.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 시 Refresh Token을 삭제합니다.")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenDto tokenDto) {
        try {
            authService.deleteRefreshToken(tokenDto.token());
            return ResponseEntity.status(HttpStatus.OK).body("로그아웃이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage()).getMessage());
        }
    }
    

    // 유저 검색
    @GetMapping("/{keyword}/{loginId}")
    @Operation(summary = "loginId 키워드로 맴버 조회", description = "로그인 아이디에 키워드가 포함된 맴버를 모두 조회합니다.")
    public ResponseEntity<List<MemberFindAllDto>> getMember(@PathVariable String keyword, @PathVariable String loginId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAllMembersByLoginIdContaining(keyword, loginId));
    }

    // 예외 처리메시지 상황별 수정 필요
    // 이메일 중복확인 추가 필요
    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입을 통해 새로운 사용자를 등록합니다.")
    public ResponseEntity<?> registerMember(@Valid @RequestBody MemberJoinDto memberJoinDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        try {
            Member registeredMember = memberService.registerMember(memberJoinDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(ErrorMessages.WRONG_INPUT_DATA.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
        }
    }

    //에러메시지 수정
    @PostMapping("/findId")
    @Operation(summary = "로그인 아이디 찾기", description = "이메일과 이름을 이용해 로그인 아이디를 찾습니다.")
    public ResponseEntity<?> findLoginId(@Valid @RequestBody FindLoginIdDto findLoginIdDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        try {
            String loginId = memberService.findLoginIdByEmailAndName(findLoginIdDto.getEmail(), findLoginIdDto.getName());
            return ResponseEntity.status(HttpStatus.OK).body(loginId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessage(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
        }
    }

    @PostMapping("/findPw")
    @Operation(summary = "비밀번호 찾기", description = "이메일과 로그인 아이디를 이용해 비밀번호 찾기 요청을 합니다.")
    public ResponseEntity<?> findPassword(@Valid @RequestBody FindPasswordDto findPasswordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        try {
            boolean exists = memberService.verifyEmailAndLoginId(findPasswordDto.getEmail(), findPasswordDto.getLoginId());
            if (exists) {
                return ResponseEntity.status(HttpStatus.OK).body("이메일과 아이디가 유효합니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessage(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
        }
    }

    @PostMapping("/resetPw/{userId}")
    @Operation(summary = "비밀번호 재설정", description = "회원의 비밀번호를 재설정합니다.")
    public ResponseEntity<?> resetPassword(@PathVariable String userId, @Valid @RequestBody ResetPasswordDto resetPasswordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        try {
            memberService.resetPassword(userId, resetPasswordDto.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
        }
    }



    @GetMapping("/id-duplicated/{loginId}")
    public ResponseEntity<?> checkLoginId(@PathVariable String loginId) {
        boolean isDuplicated = memberService.isLoginIdDuplicated(loginId);
        return new ResponseEntity<>(isDuplicated, HttpStatus.OK);
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
