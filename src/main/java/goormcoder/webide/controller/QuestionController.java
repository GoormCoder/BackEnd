package goormcoder.webide.controller;

import goormcoder.webide.dto.response.QuestionFindAllDto;
import goormcoder.webide.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/questions/all")
    @Operation(summary = "문제 조회", description = "전체 문제를 조회합니다.")
    public ResponseEntity<List<QuestionFindAllDto>> getAllQuestions() {
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getAllQuestions());
    }

}
