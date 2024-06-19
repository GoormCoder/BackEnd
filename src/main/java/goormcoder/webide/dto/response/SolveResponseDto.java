package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Solve;
import goormcoder.webide.domain.enumeration.Language;
import goormcoder.webide.domain.enumeration.SolveResult;
import java.time.LocalDateTime;

public record SolveResponseDto(
        Long id,
        String code,
        SolveResult solveResult,
        Language language,
        LocalDateTime createdAt,
        QuestionSummaryDto questionSummaryDto,
        MemberSummaryDto memberSummaryDto
) {

    public static SolveResponseDto of(Solve solve) {
        return new SolveResponseDto(
                solve.getId(),
                solve.getCode(),
                solve.getSolveResult(),
                solve.getLanguage(),
                solve.getCreatedAt(),
                QuestionSummaryDto.of(solve.getQuestion()),
                MemberSummaryDto.of(solve.getMember())
        );
    }

}
