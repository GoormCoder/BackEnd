package goormcoder.webide.repository;

import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Like;
import goormcoder.webide.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Object> findByMemberAndBoard(Member member, Board board);
}
