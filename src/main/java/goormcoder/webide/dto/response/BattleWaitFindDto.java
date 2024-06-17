package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

public record BattleWaitFindDto(
        Long roomId,
        MemberFindDto givenMember,
        MemberFindDto receivedMember,
        boolean isFull
) {
    public static BattleWaitFindDto of(Long roomId, Member givenMember, Member receivedMember, boolean isFull) {
        return new BattleWaitFindDto(
                roomId,
                MemberFindDto.from(givenMember),
                MemberFindDto.from(receivedMember),
                isFull
        );
    }
}
