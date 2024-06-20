package goormcoder.webide.controller;

import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.QuestionTag;
import goormcoder.webide.dto.request.QuestionTagCreateDto;
import goormcoder.webide.dto.request.QuestionTagsUpdateDto;
import goormcoder.webide.dto.response.QuestionTagSummaryDto;
import goormcoder.webide.service.QuestionService;
import goormcoder.webide.service.QuestionTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin/Tag", description = "태그 관련 API (어드민 전용)")
public class AdminQuestionTagController {

    private final QuestionService questionService;
    private final QuestionTagService questionTagService;
    
    @PostMapping("/tags")
    @Operation(summary = "태그 생성")
    public ResponseEntity<QuestionTagSummaryDto> createTag(@RequestBody @Valid QuestionTagCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionTagService.create(createDto));
    }

    @Operation(summary = "문제에 태그 추가")
    @PostMapping("/questions/{questionId}/tags/{tagId}")
    public ResponseEntity<List<QuestionTagSummaryDto>> addTagToQuestion(@PathVariable Long questionId, @PathVariable Long tagId) {
        Question question = questionService.findById(questionId);
        QuestionTag tag = questionTagService.findById(tagId);
        question.addTag(tag);
        return ResponseEntity.ok(
                QuestionTagSummaryDto.listOf(
                        question.getTags()
                )
        );
    }

    @Operation(summary = "문제에서 태그 삭제")
    @DeleteMapping("/questions/{questionId}/tags/{tagId}")
    public ResponseEntity<List<QuestionTagSummaryDto>> removeTagFromQuestion(@PathVariable Long questionId, @PathVariable Long tagId) {
        Question question = questionService.findById(questionId);
        QuestionTag tag = questionTagService.findById(tagId);
        question.removeTag(tag);
        return ResponseEntity.ok(
                QuestionTagSummaryDto.listOf(
                        question.getTags()
                )
        );
    }

    @Operation(summary = "문제 태그 수정")
    @PatchMapping("/questions/{questionId}/tags")
    public ResponseEntity<List<QuestionTagSummaryDto>> modifyQuestionTags(
            @PathVariable Long questionId,
            @RequestBody QuestionTagsUpdateDto updateDto
    ) {
        Question question = questionService.findById(questionId);
        List<QuestionTag> newTags = questionTagService.findAllByIds(updateDto.tagIds());
        question.replaceTags(newTags);
        return ResponseEntity.ok(
                QuestionTagSummaryDto.listOf(
                        question.getTags()
                )
        );
    }

}
