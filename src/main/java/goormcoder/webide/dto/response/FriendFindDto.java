package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

public record FriendFindDto(
        String loginId,
        String name,
        String nickname
) {
    public static FriendFindDto from(Member member) {
        return new FriendFindDto(
                member.getLoginId(),
                member.getName(),
                member.getNick()
        );
    }
}
