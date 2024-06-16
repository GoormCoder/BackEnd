package goormcoder.webide.repository;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Comment;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment findByIdOrThrow(final Long commentId) {
        return findByIdAndDeletedAtIsNull(commentId).orElseThrow(() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage()));
    }

    Optional<Comment> findByIdAndDeletedAtIsNull(Long commentId);

    @Query("select c from Comment c where c.board.id = :boardId and c.deletedAt is null")
    List<Comment> findAllByBoardId(Long boardId);
}
