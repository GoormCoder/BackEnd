package goormcoder.webide.repository;

import goormcoder.webide.domain.Solve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolveRepository extends JpaRepository<Solve, Long> {

}
