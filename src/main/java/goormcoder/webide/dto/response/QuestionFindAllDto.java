package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.enumeration.SolveResult;
import goormcoder.webide.service.QuestionService;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public record QuestionFindAllDto(
        long id,
        SolveResult state,
        String title,
        int level,
        int solved,
        int accuracy
) {

    public static List<QuestionFindAllDto> listOf(
            List<Question> questions,
            List<SolveSummaryDto> solvesByMember,
            QuestionService questionService) {

        return questions.stream()
                .map(question -> new QuestionFindAllDto(
                        question.getId(),
                        getSolveState(question.getId(), solvesByMember),
                        question.getTitle(),
                        question.getLevel(),
                        getSolvedCount(question.getId(), questionService),
                        getAccuracy(question.getId(), questionService)
                ))
                .collect(Collectors.toList());
    }

    private static SolveResult getSolveState(Long questionId, List<SolveSummaryDto> solvesByMember) {
        return solvesByMember.stream()
                .filter(solve -> solve.questionSummaryDto().id() == questionId)
                .map(SolveSummaryDto::solveResult)
                .findFirst()
                .orElse(null);

    }

    private static int getSolvedCount(long questionId, QuestionService questionService) {
        List<SolveSummaryDto> solvesByQuestionId = questionService.findSolvesById(questionId);
        return (int) solvesByQuestionId.stream()
                .filter(solve -> solve.solveResult().getMessage().equals(SolveResult.CORRECT))
                .count();
    }

    private static int getAccuracy(long questionId, QuestionService questionService) {
        List<SolveSummaryDto> solvesByQuestionId = questionService.findSolvesById(questionId);
        int size = solvesByQuestionId.size();
        if(size == 0) return 0;

        int solved = (int) solvesByQuestionId.stream()
                .filter(solve -> solve.solveResult().getMessage().equals(SolveResult.CORRECT))
                .count();

        return (solved/size)*100;
    }
}
