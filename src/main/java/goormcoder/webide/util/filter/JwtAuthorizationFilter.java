package goormcoder.webide.util.filter;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.enumeration.MemberRole;
import goormcoder.webide.jwt.JwtProvider;
import goormcoder.webide.jwt.JwtValidation;
import goormcoder.webide.security.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if(isValidToken(token)) {
                setAuthenticationContext(token);
            }
        } catch (Exception e) {
            throw new AccessDeniedException(ErrorMessages.JWT_UNAUTHORIZED_EXCEPTION.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.split(" ")[1];
    }

    private boolean isValidToken(String token) {
        return jwtProvider.validateToken(token) == JwtValidation.JWT_VALID;
    }

    private void setAuthenticationContext(String token) {
        String loginId = jwtProvider.getUsername(token);
        String role = jwtProvider.getRole(token);

        Member data = Member.builder()
                .loginId(loginId)
                .password("password")
                .role(MemberRole.valueOf(role.toUpperCase()))
                .build();

        MemberDetails memberDetails = new MemberDetails(data);
        Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
