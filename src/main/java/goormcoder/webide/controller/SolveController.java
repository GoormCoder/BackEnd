package goormcoder.webide.controller;

import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.Solve;
import goormcoder.webide.dto.request.SolveCreateDto;
import goormcoder.webide.dto.response.SolveSummaryDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.MemberService;
import goormcoder.webide.service.QuestionService;
import goormcoder.webide.service.SolveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

}
