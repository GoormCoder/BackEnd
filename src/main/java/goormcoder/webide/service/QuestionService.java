package goormcoder.webide.service;

import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.request.QuestionCreateDto;
import goormcoder.webide.repository.QuestionRepository;
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

}
