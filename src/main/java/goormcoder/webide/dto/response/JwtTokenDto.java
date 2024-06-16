package goormcoder.webide.dto.response;

public record JwtTokenDto(

        String loginId,
        String accessToken,
        String refreshToken

) {

    public static JwtTokenDto of(String loginId, String accessToken, String refreshToken) {
        return new JwtTokenDto(loginId, accessToken, refreshToken);
    }

}
