package goormcoder.webide.repository;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Question;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    default Question findByQuestionNumOrThrow(Integer questionNum) {
        return findByQuestionNum(questionNum).orElseThrow(() -> new EntityNotFoundException(ErrorMessage.QUESTION_NOT_FOUND.getMessage()));
    }

    Optional<Question> findByQuestionNum(Integer questionNum);
}
