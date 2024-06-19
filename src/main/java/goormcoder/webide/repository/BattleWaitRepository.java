package goormcoder.webide.repository;

import goormcoder.webide.domain.Battle;
import goormcoder.webide.domain.BattleWait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattleWaitRepository extends JpaRepository<BattleWait, Long> {

    @Query("select bw from BattleWait bw " +
            "where bw.level = :level and bw.language = :language and bw.isFull = false " +
            "order by bw.createdAt asc")
    List<BattleWait> findByLevelAndLanguage(
            @Param("level") Integer level,
            @Param("language") String language
    );
}
