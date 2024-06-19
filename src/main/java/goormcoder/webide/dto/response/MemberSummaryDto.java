package goormcoder.webide.dto.response;


import goormcoder.webide.domain.Member;

public record MemberSummaryDto (
        Long id,
        String loginId,
        String nick
) {
    public static MemberSummaryDto of(Member member) {
        return new MemberSummaryDto(
                member.getId(),
                member.getLoginId(),
                member.getNick()
        );
    }
}
