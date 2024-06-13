package goormcoder.webide.repository;

import goormcoder.webide.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.board.id = :boardId and c.deletedAt is null")
    List<Comment> findAllByBoardId(Long boardId);
}
