package goormcoder.webide.repository;

import goormcoder.webide.domain.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleRepository extends JpaRepository<Battle, Long> {
}
