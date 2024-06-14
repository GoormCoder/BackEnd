package goormcoder.webide.jwt;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.exception.UnauthorizedException;
import goormcoder.webide.security.MemberDetails;
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

        throw new UnauthorizedException(ErrorMessage.JWT_UNAUTHORIZED_EXCEPTION);
    }

    public String getMemberLoginId() {
        return getMemberDetails().getUsername();
    }

}
