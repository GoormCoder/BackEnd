package goormcoder.webide.controller;

import goormcoder.webide.dto.request.MemberLoginDto;
import goormcoder.webide.dto.response.JwtTokenDto;
import goormcoder.webide.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@Tag(name = "Member", description = "회원 관련 API")
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 성공 시 JWT을 발급합니다.")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody MemberLoginDto loginDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(loginDto));
    }

}