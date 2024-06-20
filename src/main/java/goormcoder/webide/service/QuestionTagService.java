package goormcoder.webide.service;

import goormcoder.webide.domain.QuestionTag;
import goormcoder.webide.dto.request.QuestionTagCreateDto;
import goormcoder.webide.dto.request.QuestionTagSummaryDto;
import goormcoder.webide.repository.QuestionTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionTagService {

    private final QuestionTagRepository questionTagRepository;

//    public QuestionTag findById(Long id) {
//        return questionTagRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.TAG_NOT_FOUND.getMessage()));
//    }

    @Transactional
    public QuestionTagSummaryDto create(QuestionTagCreateDto createDto) {
        QuestionTag tag = new QuestionTag(createDto.name());
        questionTagRepository.save(tag);
        return QuestionTagSummaryDto.of(tag);
    }

}
