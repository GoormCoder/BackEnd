package goormcoder.webide.service;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.dto.request.MemberLoginDto;
import goormcoder.webide.dto.response.JwtToken;
import goormcoder.webide.exception.UnauthorizedException;
import goormcoder.webide.jwt.JwtProvider;
import goormcoder.webide.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public JwtToken authenticate(MemberLoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginDto.loginId(), loginDto.password());
            Authentication authResult = authenticationManager.authenticate(token);
            String loginId = ((MemberDetails) authResult.getPrincipal()).getUsername();

            String accessToken = jwtProvider.issueAccessToken(authResult);
            String refreshToken = jwtProvider.issueRefreshToken(authResult);

            return JwtToken.of(loginId, accessToken, refreshToken);
        } catch (AuthenticationException e) {
            if(e instanceof BadCredentialsException) {
                throw new UnauthorizedException(ErrorMessage.JWT_BAD_CREDENTIAL_EXCEPTION);
            } else {
                throw new UnauthorizedException(ErrorMessage.JWT_UNAUTHORIZED_EXCEPTION);
            }
        }
    }

}
