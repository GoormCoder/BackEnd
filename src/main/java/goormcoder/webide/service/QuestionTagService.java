package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.QuestionTag;
import goormcoder.webide.dto.request.QuestionTagCreateDto;
import goormcoder.webide.dto.request.QuestionTagIdsDto;
import goormcoder.webide.dto.response.QuestionSummaryDto;
import goormcoder.webide.dto.response.QuestionTagSummaryDto;
import goormcoder.webide.repository.QuestionTagRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionTagService {

    private final QuestionTagRepository questionTagRepository;
    private final QuestionService questionService;

    public QuestionTag findById(Long id) {
        return questionTagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.TAG_NOT_FOUND.getMessage()));
    }

    public List<QuestionTag> findAllByIds(List<Long> ids) {
        return ids.stream()
                .map(this::findById)
                .toList();
    }

    @Transactional
    public QuestionTagSummaryDto create(QuestionTagCreateDto createDto) {
        QuestionTag tag = new QuestionTag(createDto.name());
        questionTagRepository.save(tag);
        return QuestionTagSummaryDto.of(tag);
    }

    public List<QuestionTagSummaryDto> findAll() {
        List<QuestionTag> tags = questionTagRepository.findAll();
        return (QuestionTagSummaryDto.listOf(tags));
    }

    public List<QuestionTagSummaryDto> addTagToQuestion(Long questionId, Long tagId) {
        Question question = questionService.findById(questionId);
        QuestionTag tag = this.findById(tagId);
        question.addTag(tag);
        questionService.save(question);
        List<QuestionTag> tags = question.getTags();
        return QuestionTagSummaryDto.listOf(tags);
    }

    public List<QuestionTagSummaryDto> removeTagFromQuestion(Long questionId, Long tagId) {
        Question question = questionService.findById(questionId);
        QuestionTag tag = this.findById(tagId);
        question.removeTag(tag);
        questionService.save(question);
        List<QuestionTag> tags = question.getTags();
        return QuestionTagSummaryDto.listOf(tags);
    }

    public List<QuestionTagSummaryDto> modifyQuestionTags(Long questionId, QuestionTagIdsDto updateDto) {
        List<QuestionTag> newTags = this.findAllByIds(updateDto.tagIds());
        Question question = questionService.findById(questionId);
        question.replaceTags(newTags);
        questionService.save(question);
        List<QuestionTag> tags = question.getTags();
        return QuestionTagSummaryDto.listOf(tags);
    }

    public List<Question> findAllQuestionsByTagIds(List<Long> tagIds) {
        return tagIds.stream()
                .map(this::findById)
                .map(QuestionTag::getQuestions)
                .flatMap(List::stream)
                .distinct()
                .toList();
    }

}
