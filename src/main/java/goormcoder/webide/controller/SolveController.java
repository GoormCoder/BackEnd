package goormcoder.webide.controller;

import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.Solve;
import goormcoder.webide.dto.request.SolveCreateDto;
import goormcoder.webide.dto.response.SolveResponseDto;
import goormcoder.webide.dto.response.SolveSummaryDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.MemberService;
import goormcoder.webide.service.QuestionService;
import goormcoder.webide.service.SolveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/solve")
@Tag(name = "Solve", description = "제출 관련 API")
public class SolveController {

    private final MemberService memberService;
    private final QuestionService questionService;
    private final SolveService solveService;
    private final PrincipalHandler principalHandler;

    @PostMapping("/{questionId}")
    @Operation(summary = "제출 생성")
    public ResponseEntity<SolveSummaryDto> createSolve(@PathVariable Long questionId, SolveCreateDto createDto) {
        Member member = memberService.findByLoginId(principalHandler.getMemberLoginId());
        Question question = questionService.findById(questionId);
        Solve solve = solveService.create(question, member, createDto);
        return ResponseEntity.ok(SolveSummaryDto.of(solve));
    }

    @GetMapping("/{solveId}")
    @Operation(summary = "제출 열람", description = "본인 혹은 맞은 사람만 열람 가능")
    public ResponseEntity<SolveResponseDto> getSolve(@PathVariable Long solveId) {
        Member member = memberService.findByLoginId(principalHandler.getMemberLoginId());
        Solve solve = solveService.findById(solveId);
        solveService.checkCanAccess(solve, member);
        return ResponseEntity.ok(SolveResponseDto.of(solve));
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "제출 조회", description = "문제에 대한 모든 제출 조회")
    public ResponseEntity<List<SolveSummaryDto>> getAllSolves(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.findSolvesById(questionId));
    }

}
