package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.response.QuestionFindDto;
import goormcoder.webide.dto.response.QuestionFindAllDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Question", description = "문제 관련 API")
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final PrincipalHandler principalHandler;
    @GetMapping()
    @Operation(summary = "문제 조회", description = "전체 문제를 조회합니다.")
    public ResponseEntity<List<QuestionFindAllDto>> getAllQuestions() {
        String loginId = principalHandler.getMemberLoginId();
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getAllQuestions(loginId));
    }

    @Operation(summary = "문제 열람")
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionFindDto> findById(@PathVariable Long questionId) {
        Question question = questionService.findById(questionId);
        return ResponseEntity.ok(QuestionFindDto.of(question));
    }

}
