package goormcoder.webide.repository;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Question;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    default Question findByQuestionNumOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.QUESTION_NOT_FOUND.getMessage()));
    }

    List<Question> findAll();

    List<Question> findByLevel(Integer level);
}
