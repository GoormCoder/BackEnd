package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.TestCase;
import goormcoder.webide.dto.request.TestCaseCreateDto;
import goormcoder.webide.dto.request.TestCaseUpdateDto;
import goormcoder.webide.dto.response.TestCaseFindDto;
import goormcoder.webide.service.QuestionService;
import goormcoder.webide.service.TestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "Admin/TestCase", description = "테스트케이스 관련 API (어드민 전용)")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminTestCaseController {

    private final QuestionService questionService;
    private final TestCaseService testCaseService;

    @Operation(summary = "테스트케이스 추가")
    @PostMapping("/questions/{questionId}/testcases")
    public ResponseEntity<String> createTestCase(@PathVariable Long questionId, @RequestBody TestCaseCreateDto createDto) {
        Question question = questionService.findById(questionId);
        TestCase testCase = testCaseService.create(question, createDto.input(), createDto.output());
        return ResponseEntity.status(HttpStatus.CREATED).body(testCase.toString());
    }

    @Operation(summary = "테스트케이스 수정")
    @PatchMapping("/testcases/{testcaseId}")
    public ResponseEntity<String> updateTestCase(@PathVariable Long testcaseId, @RequestBody TestCaseUpdateDto updateDto) {
        TestCase testCase = testCaseService.update(testcaseId, updateDto.input(), updateDto.output());
        return ResponseEntity.ok(testCase.toString());
    }

    @Operation(summary = "테스트케이스 삭제")
    @DeleteMapping("/testcases/{testcaseId}")
    public ResponseEntity<String> deleteTestCase(@PathVariable Long questionId, @PathVariable Long testcaseId) {
        testCaseService.deleteById(testcaseId);
        return ResponseEntity.ok("deleted");
    }

    @Operation(summary = "문제의 전체 테스트케이스 조회")
    @GetMapping("/questions/{questionId}/testcases")
    public ResponseEntity<Page<TestCaseFindDto>> findAllTestCasesByQuestionId(@PathVariable Long questionId, Pageable pageable) {
        return ResponseEntity.ok(testCaseService.findAllByQuestionId(questionId, pageable));
    }

}
