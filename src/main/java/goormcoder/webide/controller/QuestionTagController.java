package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.QuestionTag;
import goormcoder.webide.dto.request.QuestionTagIdsDto;
import goormcoder.webide.dto.response.QuestionSummaryDto;
import goormcoder.webide.dto.response.QuestionTagSummaryDto;
import goormcoder.webide.service.QuestionService;
import goormcoder.webide.service.QuestionTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tag", description = "태그 관련 API")
public class QuestionTagController {

    private final QuestionService questionService;
    private final QuestionTagService questionTagService;

    @Operation(summary = "문제의 태그 조회")
    @GetMapping("/questions/{questionId}/tags")
    public ResponseEntity<List<QuestionTagSummaryDto>> findAllTags(@PathVariable Long questionId) {
        Question question = questionService.findById(questionId);
        List<QuestionTag> tags = question.getTags();
        List<QuestionTagSummaryDto> tagDtos = QuestionTagSummaryDto.listOf(tags);
        return ResponseEntity.ok(tagDtos);
    }

    @Operation(summary = "해당 태그들을 가진 문제 조회")
    @GetMapping("/questions/tags")
    public ResponseEntity<List<QuestionSummaryDto>> findAllQuestionsByTags(@RequestBody QuestionTagIdsDto tagIdsDto) {
        List<Question> questions = questionTagService.findAllQuestionsByTagIds(tagIdsDto.tagIds());
        List<QuestionSummaryDto> dtos = QuestionSummaryDto.listOf(questions);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "모든 태그 조회")
    @GetMapping("/tags")
    public ResponseEntity<List<QuestionTagSummaryDto>> findAllTags() {
        List<QuestionTag> tags = questionTagService.findAll();
        List<QuestionTagSummaryDto> dtos = QuestionTagSummaryDto.listOf(tags);
        return ResponseEntity.ok(dtos);
    }

}
