package goormcoder.webide.repository;

import goormcoder.webide.domain.Battle;
import goormcoder.webide.domain.BattleWait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattleWaitRepository extends JpaRepository<BattleWait, Long> {

    List<BattleWait> findByLevelAndLanguageAndIsFullFalseOrderByCreatedAtAsc(Integer level, String language);
}
