package goormcoder.webide.controller;

import goormcoder.webide.dto.request.RefreshTokenDto;
import goormcoder.webide.dto.response.AccessTokenDto;
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
@RequestMapping("/auth")
@Tag(name = "Auth", description = "JWT 관련 API")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "Refresh Token이 유효한 경우 Access Token을 재발급합니다.")
    public ResponseEntity<AccessTokenDto> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        AccessTokenDto accessTokenDto = authService.reissueToken(refreshTokenDto.token());
        if(accessTokenDto.reAuthenticationRequired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accessTokenDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(accessTokenDto);
    }

}
