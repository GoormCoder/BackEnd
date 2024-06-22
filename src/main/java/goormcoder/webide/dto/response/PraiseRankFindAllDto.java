package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record PraiseRankFindAllDto(
        String loginId,
        String name,
        ScoreDto type
) {
    public static List<PraiseRankFindAllDto> listOf(List<Member> members) {
        AtomicInteger rank = new AtomicInteger(1);
        return members
                .stream()
                .map(member -> new PraiseRankFindAllDto(
                        member.getLoginId(),
                        member.getName(),
                        ScoreDto.of(member.getPraiseScore(), rank.getAndIncrement())
                )).toList();
    }
}
