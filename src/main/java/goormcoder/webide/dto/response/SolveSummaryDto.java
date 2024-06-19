package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Solve;
import goormcoder.webide.domain.enumeration.SolveResult;
import java.util.List;

public record SolveSummaryDto(
    QuestionSummaryDto questionSummaryDto,
    MemberSummaryDto memberSummaryDto,
    SolveResult solveResult
) {
    public static SolveSummaryDto of(Solve solve) {
        return new SolveSummaryDto(
                QuestionSummaryDto.of(solve.getQuestion()),
                MemberSummaryDto.of(solve.getMember()),
                solve.getSolveResult()
        );
    }

    public static List<SolveSummaryDto> listOf(List<Solve> solves) {
        return solves.stream()
                .map(SolveSummaryDto::of)
                .toList();
    }
}
