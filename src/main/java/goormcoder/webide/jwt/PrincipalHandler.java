package goormcoder.webide.jwt;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.security.MemberDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalHandler {

    private MemberDetails getMemberDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof MemberDetails) {
            return (MemberDetails) authentication.getPrincipal();
        }

        throw new AccessDeniedException(ErrorMessage.JWT_UNAUTHORIZED_EXCEPTION.getMessage());
    }

    public String getMemberLoginId() {
        return getMemberDetails().getUsername();
    }

}
