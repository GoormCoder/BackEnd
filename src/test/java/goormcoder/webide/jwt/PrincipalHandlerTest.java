package goormcoder.webide.jwt;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.security.MemberDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrincipalHandlerTest {

    @Mock
    private MemberDetails memberDetails;

    private PrincipalHandler principalHandler;

    @BeforeEach
    void init() {
        principalHandler = new PrincipalHandler();
        setSecurityContext(memberDetails);
    }

    @Test
    void getMemberLoginId() {
        when(memberDetails.getUsername()).thenReturn("test-member");
        String loginId = principalHandler.getMemberLoginId();
        assertEquals("test-member", loginId);
    }

    @Test
    void testGetMemberDetails_Unauthorized() {
        SecurityContextHolder.clearContext();

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            principalHandler.getMemberLoginId();
        });

        assertEquals(ErrorMessage.JWT_UNAUTHORIZED_EXCEPTION.getMessage(), exception.getMessage());
    }

    private void setSecurityContext(MemberDetails memberDetails) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(memberDetails, null));
        SecurityContextHolder.setContext(securityContext);
    }

}
