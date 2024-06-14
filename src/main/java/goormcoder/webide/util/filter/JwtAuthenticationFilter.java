package goormcoder.webide.util.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import goormcoder.webide.dto.request.MemberLoginDto;
import goormcoder.webide.dto.response.JwtToken;
import goormcoder.webide.jwt.JwtProvider;
import goormcoder.webide.security.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/members/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            MemberLoginDto loginRequest = new ObjectMapper().readValue(request.getInputStream(), MemberLoginDto.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequest.loginId(), loginRequest.password()
            );
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("[JwtAuthenticationFilter] login success");

        String loginId = ((MemberDetails) authResult.getPrincipal()).getUsername();

        String accessToken = jwtProvider.issueAccessToken(authResult);
        String refreshToken = jwtProvider.issueRefreshToken(authResult);

        JwtToken jwtToken = JwtToken.of(loginId, accessToken, refreshToken);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new ObjectMapper().writeValueAsString(jwtToken));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("[JwtAuthenticationFilter] login fail");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + failed.getMessage() + "\"}");
    }

}
