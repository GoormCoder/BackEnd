package goormcoder.webide.util.interceptor;

import goormcoder.webide.constants.ErrorMessages;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if(request.getPrincipal() == null) {
            throw new AccessDeniedException(ErrorMessages.JWT_UNAUTHORIZED_EXCEPTION.getMessage());
        }

        String loginId = request.getPrincipal().getName();
        attributes.put("loginId", loginId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
