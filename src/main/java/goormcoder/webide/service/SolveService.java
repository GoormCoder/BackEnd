package goormcoder.webide.service;

import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.Solve;
import goormcoder.webide.domain.enumeration.SolveResult;
import goormcoder.webide.dto.TestCaseValueDto;
import goormcoder.webide.dto.request.SolveCreateDto;
import goormcoder.webide.repository.SolveRepository;
import goormcoder.webide.util.javaCompiler.ScoringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SolveService {

    private final SolveRepository solveRepository;

    @Transactional
    public Solve create(Question question, Member member, SolveCreateDto createDto) {
        SolveResult solveResult = ScoringUtil.getSolveResult(
                createDto.code(),
                question.getTestCases()
                        .stream()
                        .map(TestCaseValueDto::of)
                        .toList()
        );

        Solve solve = new Solve(
                question,
                member,
                createDto.code(),
                createDto.language(),
                solveResult
        );

        solveRepository.save(solve);

        return solve;
    }

}
