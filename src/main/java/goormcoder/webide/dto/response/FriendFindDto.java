package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

import java.time.LocalDate;

public record FriendFindDto(
        String loginId,
        String name,
        String nickname,
        String email,
        String info,
        LocalDate birth
) {
    public static FriendFindDto from(Member member) {
        return new FriendFindDto(
                member.getLoginId(),
                member.getName(),
                member.getNick(),
                member.getEmail(),
                member.getInfo(),
                member.getBirth()
        );
    }
}
