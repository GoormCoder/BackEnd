package goormcoder.webide.repository;

import goormcoder.webide.domain.BattleWait;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.Solve;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolveRepository extends JpaRepository<Solve, Long> {

    List<Solve> findAllByIdAndMember(Long id, Member member);

    @Query("select s from Solve s " +
            "where s.member = :member and s.question = :question and s.solveResult = 'CORRECT'")
    List<Solve> findCorrectByMemberIdAndQuestId(
            @Param("member") Member member,
            @Param("question") Question question
    );
}
