package goormcoder.webide.dto.response;

public record JwtToken(

        String loginId,
        String accessToken,
        String refreshToken

) {

    public static JwtToken of(String loginId, String accessToken, String refreshToken) {
        return new JwtToken(loginId, accessToken, refreshToken);
    }

}
