package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Battle;
import goormcoder.webide.domain.Member;

import java.util.List;

public record BattleRecordFindDto(
        String givenUser,
        String receivedUser,
        String result
) {
    public static List<BattleRecordFindDto> listOf(Member member, List<Battle> battles) {
        return battles.stream()
                .map(battle -> {
                    String result = battle.getWinner().getId().equals(member.getId()) ? "WIN" : "LOSE";
                    return new BattleRecordFindDto(
                            battle.getGivenMember().getNick(),
                            battle.getReceivedMember().getNick(),
                            result
                    );
                }).toList();
    }
}
