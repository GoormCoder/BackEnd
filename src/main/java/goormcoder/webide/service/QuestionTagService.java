package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.QuestionTag;
import goormcoder.webide.dto.request.QuestionTagCreateDto;
import goormcoder.webide.dto.response.QuestionTagSummaryDto;
import goormcoder.webide.repository.QuestionTagRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionTagService {

    private final QuestionTagRepository questionTagRepository;

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

}
