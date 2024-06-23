package goormcoder.webide.repository;

import goormcoder.webide.domain.TestCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    Page<TestCase> findAllByQuestionId(Long id, PageRequest pageRequest);

}
