package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.response.QuestionFindDto;
import goormcoder.webide.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "문제 열람")
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionFindDto> findById(@PathVariable Long questionId) {
        Question question = questionService.findById(questionId);
        return ResponseEntity.ok(QuestionFindDto.of(question));
    }

}
