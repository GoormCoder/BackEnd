package goormcoder.webide.controller;

import goormcoder.webide.dto.request.QuestionTagCreateDto;
import goormcoder.webide.dto.request.QuestionTagSummaryDto;
import goormcoder.webide.service.QuestionTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin/Tag", description = "태그 관련 API (어드민 전용)")
public class AdminQuestionTagController {

    private final QuestionTagService questionTagService;
    
    @PostMapping("/tags")
    @Operation(summary = "태그 생성")
    public ResponseEntity<QuestionTagSummaryDto> createTag(QuestionTagCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionTagService.create(createDto));
    }

}
