package goormcoder.webide.repository;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Comment;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment findByIdOrThrow(final Long commentId) {
        return findByIdAndDeletedAtIsNull(commentId).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.COMMENT_NOT_FOUND.getMessage()));
    }

    Optional<Comment> findByIdAndDeletedAtIsNull(Long commentId);

    Page<Comment> findAllByDeletedAtIsNullAndBoardId(Long boardId, Pageable pageable);
}
