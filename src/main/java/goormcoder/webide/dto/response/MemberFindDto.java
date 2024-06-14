package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

public record MemberFindDto(
        Long id,
        String longinId
) {
    public static MemberFindDto from(Member member) {
        return new MemberFindDto(
                member.getId(),
                member.getLoginId()
        );
    }
}
