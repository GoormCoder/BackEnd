package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Battle;
import goormcoder.webide.domain.Member;

import java.util.List;

public record BattleRecordFindAllDto(
        String nickname,
        int battleScore,
        String totalResult,
        String winRate,
        List<BattleRecordFindDto> battleRecords

) {
    public static BattleRecordFindAllDto of(Member member, List<Battle> battles, String totalResult, String winRate) {
        return new BattleRecordFindAllDto(
                member.getNick(),
                member.getBattleScore(),
                totalResult,
                winRate,
                BattleRecordFindDto.listOf(member, battles)
        );
    }
}
