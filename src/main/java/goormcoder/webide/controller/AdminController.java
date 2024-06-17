package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.TestCase;
import goormcoder.webide.dto.request.QuestionCreateDto;
import goormcoder.webide.dto.request.QuestionUpdateDto;
import goormcoder.webide.dto.request.TestCaseCreateDto;
import goormcoder.webide.dto.request.TestCaseUpdateDto;
import goormcoder.webide.dto.response.QuestionSummaryDto;
import goormcoder.webide.dto.response.TestCaseFindDto;
import goormcoder.webide.service.QuestionService;
import goormcoder.webide.service.TestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final QuestionService questionService;
    private final TestCaseService testCaseService;

    @Operation(summary = "문제 생성")
    @PostMapping("/questions")
    public ResponseEntity<QuestionSummaryDto> createQuestion(@RequestBody @Valid QuestionCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.create(createDto));
    }

    @Operation(summary = "문제 수정")
    @PatchMapping("/questions/{questionId}")
    public ResponseEntity<QuestionSummaryDto> updateQuestion(@PathVariable @Valid Long questionId, @RequestBody @Valid QuestionUpdateDto updateDto) {
        return ResponseEntity.ok(questionService.update(questionId, updateDto));
    }

    @Operation(summary = "문제 삭제")
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId) {
        questionService.delete(questionId);
        return ResponseEntity.ok("deleted");
    }

    @Operation(summary = "테스트케이스 추가")
    @PostMapping("/questions/{questionId}/testcases")
    public ResponseEntity<String> createTestCase(@PathVariable Long questionId, @RequestBody TestCaseCreateDto createDto) {
        Question question = questionService.findById(questionId);
        TestCase testCase = testCaseService.create(question, createDto.input(), createDto.output());
        return ResponseEntity.status(HttpStatus.CREATED).body(testCase.toString());
    }

    @Operation(summary = "테스트케이스 수정")
    @PatchMapping("/questions/{questionId}/testcases/{testcaseId}")
    public ResponseEntity<String> updateTestCase(
            @PathVariable Long questionId,
            @PathVariable Long testcaseId,
            @RequestBody TestCaseUpdateDto updateDto
    ) {
        TestCase testCase = testCaseService.update(testcaseId, updateDto.input(), updateDto.output());
        return ResponseEntity.ok(testCase.toString());
    }

    @Operation(summary = "테스트케이스 삭제")
    @DeleteMapping("/questions/{questionId}/testcases/{testcaseId}")
    public ResponseEntity<String> deleteTestCase(@PathVariable Long questionId, @PathVariable Long testcaseId) {
        testCaseService.deleteById(testcaseId);
        return ResponseEntity.ok("deleted");
    }

    @Operation(summary = "테스트케이스 전체 조회")
    @GetMapping("/questions/{questionId}/testcases/all")
    public ResponseEntity<List<TestCaseFindDto>> findAllTestCases(@PathVariable Long questionId) {
        List<TestCaseFindDto> response = questionService.findAllTestCasesById(questionId);
        return ResponseEntity.ok(response);
    }
}
