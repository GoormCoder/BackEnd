package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.Solve;
import goormcoder.webide.domain.enumeration.MemberRole;
import goormcoder.webide.domain.enumeration.SolveResult;
import goormcoder.webide.dto.TestCaseValueDto;
import goormcoder.webide.dto.request.SolveCreateDto;
import goormcoder.webide.repository.SolveRepository;
import goormcoder.webide.util.javaCompiler.ScoringUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SolveService {

    private final SolveRepository solveRepository;

    @Transactional
    public Solve create(Question question, Member member, SolveCreateDto createDto) {
        SolveResult solveResult;
        switch (createDto.language()) {
            case JAVA :
                solveResult = ScoringUtil.getSolveResult(
                    createDto.code(),
                    question.getTestCases()
                            .stream()
                            .map(TestCaseValueDto::of)
                            .toList()
                );
                break;

            default : throw new IllegalArgumentException(ErrorMessages.UNAVAILABLE_LANGUAGE.getMessage());
        }

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

    public Solve findById(Long id) {
        return solveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.SOLVE_NOT_FOUND.getMessage()));
    }

    public void checkCanAccess(Solve solve, Member member) {
        if (member.equals(solve.getMember()) || member.getRole().equals(MemberRole.ROLE_ADMIN)) {
            return;
        }
        if (solveRepository.findAllByIdAndMember(solve.getId(), member).isEmpty()) {
            throw new AccessDeniedException(ErrorMessages.FORBIDDEN_SOLVE_ACCESS.getMessage());
        }
    }

}
