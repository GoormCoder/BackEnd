package goormcoder.webide.util.interceptor;


import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.enumeration.MemberRole;
import goormcoder.webide.jwt.JwtProvider;
import goormcoder.webide.jwt.JwtValidation;
import goormcoder.webide.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("Authorization");
            if(authorization != null && !authorization.isEmpty()) {
                String token = authorization.get(0).split(" ")[1];
                if(isValidToken(token)) {
                    setAuthenticationContext(token);
                }
            }
        }

        return message;
    }

    private boolean isValidToken(String token) {
        return jwtProvider.validateToken(token) == JwtValidation.JWT_VALID;
    }

    private void setAuthenticationContext(String token) {
        Member data = Member.builder()
                .loginId(jwtProvider.getUsername(token))
                .password("password")
                .role(MemberRole.valueOf(jwtProvider.getRole(token).toUpperCase()))
                .build();

        MemberDetails memberDetails = new MemberDetails(data);
        Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
