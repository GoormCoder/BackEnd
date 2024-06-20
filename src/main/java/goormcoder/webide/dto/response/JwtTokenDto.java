package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

public record JwtTokenDto(

        String loginId,
        String nickname,
        String accessToken,
        String refreshToken

) {

    public static JwtTokenDto of(Member member, String accessToken, String refreshToken) {
        return new JwtTokenDto(
                member.getLoginId(),
                member.getNick(),
                accessToken,
                refreshToken
        );
    }

}
