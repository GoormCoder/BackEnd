package goormcoder.webide.controller;

import goormcoder.webide.dto.request.QuestionCreateDto;
import goormcoder.webide.dto.request.QuestionUpdateDto;
import goormcoder.webide.dto.response.QuestionSummaryDto;
import goormcoder.webide.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin/Question", description = "문제 관련 API (어드민 전용)")
@RequestMapping("/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final QuestionService questionService;

    @Operation(summary = "문제 생성")
    @PostMapping()
    public ResponseEntity<QuestionSummaryDto> createQuestion(@RequestBody @Valid QuestionCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.create(createDto));
    }

    @Operation(summary = "문제 수정")
    @PatchMapping("/{questionId}")
    public ResponseEntity<QuestionSummaryDto> updateQuestion(@PathVariable @Valid Long questionId, @RequestBody @Valid QuestionUpdateDto updateDto) {
        return ResponseEntity.ok(questionService.update(questionId, updateDto));
    }

    @Operation(summary = "문제 삭제")
    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId) {
        questionService.delete(questionId);
        return ResponseEntity.ok("deleted");
    }

}
