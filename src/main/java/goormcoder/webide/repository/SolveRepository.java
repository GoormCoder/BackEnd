package goormcoder.webide.repository;

import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Solve;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolveRepository extends JpaRepository<Solve, Long> {

    List<Solve> findAllByIdAndMember(Long id, Member member);

}
