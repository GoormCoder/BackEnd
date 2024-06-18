package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Battle;
import goormcoder.webide.domain.Question;

public record BattleInfoDto(
        Long battleId,
        QuestionSummaryDto question
) {
    public static BattleInfoDto of(Battle battle, Question question) {
        return new BattleInfoDto(
                battle.getId(),
                QuestionSummaryDto.of(question)
        );
    }
}
