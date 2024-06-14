package goormcoder.webide.repository;

import goormcoder.webide.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {


    Optional<Question> findByQuestionNum(Integer integer);
}
