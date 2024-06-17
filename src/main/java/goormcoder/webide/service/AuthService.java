package goormcoder.webide.service;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.RefreshToken;
import goormcoder.webide.dto.request.MemberLoginDto;
import goormcoder.webide.dto.response.AccessTokenDto;
import goormcoder.webide.dto.response.JwtTokenDto;
import goormcoder.webide.jwt.JwtProvider;
import goormcoder.webide.repository.RefreshTokenRepository;
import goormcoder.webide.security.MemberDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenDto authenticate(MemberLoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginDto.loginId(), loginDto.password());
            Authentication authResult = authenticationManager.authenticate(token);
            String loginId = ((MemberDetails) authResult.getPrincipal()).getUsername();

            String accessToken = jwtProvider.issueAccessToken(authResult);
            String refreshToken = jwtProvider.issueRefreshToken(authResult);

            saveRefreshToken(refreshToken);

            return JwtTokenDto.of(loginId, accessToken, refreshToken);
        } catch (AuthenticationException e) {
            if(e instanceof BadCredentialsException) {
                throw new AccessDeniedException(ErrorMessage.JWT_BAD_CREDENTIAL_EXCEPTION.getMessage());
            } else {
                throw new AccessDeniedException(ErrorMessage.JWT_UNAUTHORIZED_EXCEPTION.getMessage());
            }
        }
    }

    @Transactional
    public AccessTokenDto reissueToken(String refreshToken) {
        try {
            if(!refreshTokenRepository.existsByToken(refreshToken)) {
                return AccessTokenDto.of(true, ErrorMessage.TOKEN_NOT_FOUND.getMessage(), null, null);
            }

            Authentication auth = jwtProvider.getAuthenticationFromToken(refreshToken);
            String newAccessToken = jwtProvider.issueAccessToken(auth);
            String loginId = ((MemberDetails) auth.getPrincipal()).getUsername();

            return AccessTokenDto.of(false, null, loginId, newAccessToken);
        } catch (ExpiredJwtException e) {
            refreshTokenRepository.deleteByToken(refreshToken);
            return AccessTokenDto.of(true, ErrorMessage.TOKEN_EXPIRED.getMessage(), null, null);
        } catch (Exception e) {
            return AccessTokenDto.of(true, ErrorMessage.INTERNAL_SERVER_ERROR.getMessage(), null, null);
        }
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        if(!refreshTokenRepository.existsByToken(refreshToken)) {
            throw new EntityNotFoundException(ErrorMessage.TOKEN_NOT_FOUND.getMessage());
        }

        refreshTokenRepository.deleteByToken(refreshToken);
    }

    private void saveRefreshToken(String refreshToken) {
        refreshTokenRepository.save(RefreshToken.of(refreshToken));
    }

}
