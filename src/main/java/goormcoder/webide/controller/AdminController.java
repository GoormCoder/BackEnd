package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.request.QuestionCreateDto;
import goormcoder.webide.dto.request.QuestionUpdateDto;
import goormcoder.webide.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "문제 생성")
    @PostMapping("/questions")
    public ResponseEntity<String> createQuestion(@Valid @RequestBody QuestionCreateDto createDto) {
        Question question = questionService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(question.getFormattedTitle());
    }

    @Operation(summary = "문제 수정")
    @PatchMapping("/questions/{questionId}")
    public ResponseEntity<String> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody QuestionUpdateDto updateDto
    ) {
        Question question = questionService.update(
                questionId,
                updateDto.level(),
                updateDto.title(),
                updateDto.content()
        );
        return ResponseEntity.ok(question.getFormattedTitle());
    }

}
