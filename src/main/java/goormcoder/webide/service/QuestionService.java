package goormcoder.webide.service;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.request.QuestionCreateDto;
import goormcoder.webide.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public Question createQuestion(QuestionCreateDto createDto) {
        Question question = new Question(
                createDto.title(),
                createDto.content(),
                createDto.level()
        );
        questionRepository.save(question);
        return question;
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.QUESTION_NOT_FOUND.getMessage()));
    }

}
