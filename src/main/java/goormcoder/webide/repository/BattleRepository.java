package goormcoder.webide.repository;

import goormcoder.webide.domain.Battle;
import goormcoder.webide.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BattleRepository extends JpaRepository<Battle, Long> {

    @Query("select b from Battle b " +
            "where b.givenMember = :member or b.receivedMember = :member " +
            "order by b.createdAt desc")
    List<Battle> findByMember(@Param("member") Member member, Pageable pageable);

    int countByWinner(Member member);

    @Query("select count(b) from Battle b " +
            "where b.givenMember = :member or b.receivedMember = :member")
    int countByMember(@Param("member") Member member);
}
