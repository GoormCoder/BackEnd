package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.QuestionTag;
import goormcoder.webide.dto.request.QuestionTagCreateDto;
import goormcoder.webide.dto.request.QuestionTagIdsDto;
import goormcoder.webide.dto.request.QuestionTagUpdateDto;
import goormcoder.webide.dto.response.QuestionTagSummaryDto;
import goormcoder.webide.repository.QuestionTagRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

    @Transactional
    public QuestionTagSummaryDto update(Long tagId, QuestionTagUpdateDto updateDto) {
        QuestionTag tag = new QuestionTag(updateDto.name());
        tag.rename(updateDto.name());
        questionTagRepository.save(tag);
        return QuestionTagSummaryDto.of(tag);
    }

    @Transactional
    public void delete(Long id) {
        QuestionTag tag = this.findById(id);
        questionTagRepository.delete(tag);
    }

    public List<QuestionTag> findAll() {
        List<QuestionTag> tags = questionTagRepository.findAll();
        return (tags);
    }

    public List<QuestionTagSummaryDto> addTagToQuestion(Long questionId, Long tagId) {
        Question question = questionService.findById(questionId);
        QuestionTag tag = this.findById(tagId);
        question.addTag(tag);
        questionService.save(question);
        questionTagRepository.save(tag);
        Collection<QuestionTag> tags = question.getTags();
        return QuestionTagSummaryDto.listOf(tags);
    }

    public List<QuestionTagSummaryDto> removeTagFromQuestion(Long questionId, Long tagId) {
        Question question = questionService.findById(questionId);
        QuestionTag tag = this.findById(tagId);
        question.removeTag(tag);
        questionService.save(question);
        questionTagRepository.save(tag);
        Collection<QuestionTag> tags = question.getTags();
        return QuestionTagSummaryDto.listOf(tags);
    }

    public List<QuestionTagSummaryDto> modifyQuestionTags(Long questionId, QuestionTagIdsDto updateDto) {
        List<QuestionTag> newTags = this.findAllByIds(updateDto.tagIds());
        Question question = questionService.findById(questionId);
        question.replaceTags(newTags);
        questionService.save(question);
        Set<QuestionTag> tags = question.getTags();
        return QuestionTagSummaryDto.listOf(tags);
    }
    public List<Question> findAllQuestionsByTagIds(Collection<Long> tagIds) {
        List<Question> filteredQuestions = new ArrayList<>();

        List<Set<Question>> foundQuestions = tagIds.stream()
                .map(this::findById)
                .map(QuestionTag::getQuestions)
                .toList();

        int tagCount = foundQuestions.size();
        for (Question question : foundQuestions.get(0)) {
            for (int i = 1; i < tagCount; i++) {
                if (!foundQuestions.get(i).contains(question)) {
                    continue;
                }
                filteredQuestions.add(question);
            }
        }

        return filteredQuestions.stream().toList();
    }

}
