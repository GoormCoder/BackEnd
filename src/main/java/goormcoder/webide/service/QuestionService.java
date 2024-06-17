package goormcoder.webide.service;

import goormcoder.webide.dto.response.QuestionFindAllDto;
import goormcoder.webide.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    //게시글 조회
    @Transactional(readOnly = true)
    public List<QuestionFindAllDto> getAllQuestions() {
        return QuestionFindAllDto.listOf(questionRepository.findAll());
    }
}
