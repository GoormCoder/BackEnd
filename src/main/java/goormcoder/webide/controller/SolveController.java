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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Solve", description = "풀이 관련 API")
public class SolveController {

    private final MemberService memberService;
    private final QuestionService questionService;
    private final SolveService solveService;
    private final PrincipalHandler principalHandler;

    @PostMapping("/questions/{questionId}/solves")
    @Operation(summary = "풀이 생성")
    public ResponseEntity<SolveSummaryDto> createSolve(@PathVariable Long questionId, @RequestBody SolveCreateDto createDto) {
        Member member = memberService.findByLoginId(principalHandler.getMemberLoginId());
        Question question = questionService.findById(questionId);
        Solve solve = solveService.create(question, member, createDto);
        return ResponseEntity.ok(SolveSummaryDto.of(solve));
    }

    @GetMapping("/solves/{solveId}")
    @Operation(summary = "풀이 열람", description = "본인 혹은 맞은 사람만 열람 가능")
    public ResponseEntity<SolveResponseDto> getSolve(@PathVariable Long solveId) {
        Member member = memberService.findByLoginId(principalHandler.getMemberLoginId());
        Solve solve = solveService.findById(solveId);
        solveService.checkCanAccess(solve, member);
        return ResponseEntity.ok(SolveResponseDto.of(solve));
    }

    @GetMapping("/questions/{questionId}/solves")
    @Operation(summary = "문제의 풀이 조회", description = "특정 문제에 제출된 모든 풀이 조회")
    public ResponseEntity<List<SolveSummaryDto>> getAllSolvesByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.findSolvesById(questionId));
    }

    @GetMapping("/members/{loginId}/solves")
    @Operation(summary = "사용자의 풀이 조회", description = "loginId로 조회")
    public ResponseEntity<List<SolveSummaryDto>> getAllSolvesByMember(@PathVariable String loginId) {
        return ResponseEntity.ok(memberService.findSolvesByLoginId(loginId));
    }

}
