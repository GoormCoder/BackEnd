package goormcoder.webide.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record AccessTokenDto(

        boolean reAuthenticationRequired,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String reAuthenticationRequiredCause,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String loginId,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String accessToken

) {

    public static AccessTokenDto of(boolean reAuthenticationRequired, String reAuthenticationRequiredCause, String loginId, String accessToken) {
        return new AccessTokenDto(reAuthenticationRequired, reAuthenticationRequiredCause, loginId, accessToken);
    }

}
