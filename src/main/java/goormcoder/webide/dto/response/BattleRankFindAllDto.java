package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record BattleRankFindAllDto(
        String loginId,
        String name,
        ScoreDto type
) {
    public static List<BattleRankFindAllDto> listOf(List<Member> members) {
        AtomicInteger rank = new AtomicInteger(1);
        return members
                .stream()
                .map(member -> new BattleRankFindAllDto(
                        member.getLoginId(),
                        member.getName(),
                        ScoreDto.of(member.getBattleScore(), rank.getAndIncrement())
                )).toList();
    }
}