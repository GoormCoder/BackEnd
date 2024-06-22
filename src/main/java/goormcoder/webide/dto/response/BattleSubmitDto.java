package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Battle;
import goormcoder.webide.domain.Solve;
import goormcoder.webide.domain.enumeration.SolveResult;

public record BattleSubmitDto(
        QuestionSummaryDto questionSummaryDto,
        MemberSummaryDto memberSummaryDto,
        SolveResult solveResult,
        String solveResultMessage,
        String battleResult
) {
    public static BattleSubmitDto of(Solve solve, String battleResult) {
        return new BattleSubmitDto(
                QuestionSummaryDto.of(solve.getQuestion()),
                MemberSummaryDto.of(solve.getMember()),
                solve.getSolveResult(),
                solve.getSolveResult().getMessage(),
                battleResult
        );
    }
}
