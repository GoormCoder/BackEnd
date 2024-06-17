package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.TestCase;
import goormcoder.webide.dto.request.QuestionCreateDto;
import goormcoder.webide.dto.request.QuestionUpdateDto;
import goormcoder.webide.dto.request.TestCaseCreateDto;
import goormcoder.webide.service.QuestionService;
import goormcoder.webide.service.TestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<String> createQuestion(@RequestBody @Valid QuestionCreateDto createDto) {
        Question question = questionService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(question.getFormattedTitle());
    }

    @Operation(summary = "문제 수정")
    @PatchMapping("/questions/{questionId}")
    public ResponseEntity<String> updateQuestion(
            @PathVariable @Valid Long questionId,
            @RequestBody @Valid QuestionUpdateDto updateDto
    ) {
        Question question = questionService.update(
                questionId,
                updateDto.level(),
                updateDto.title(),
                updateDto.content()
        );
        return ResponseEntity.ok(question.getFormattedTitle());
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
}
