package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

import java.util.List;

public record MemberFindAllDto(
        String loginId,
        String nickname
) {
    public static List<MemberFindAllDto> listOf(List<Member> members) {
        return members
                .stream()
                .map(member -> new MemberFindAllDto(
                        member.getLoginId(),
                        member.getNick()
                )).toList();
    }
}
