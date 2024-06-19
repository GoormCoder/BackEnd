package goormcoder.webide.repository;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.enumeration.BoardType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    default Board findByIdOrThrow(final Long boardId) {
        return findByIdAndDeletedAtIsNull(boardId).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.BOARD_NOT_FOUND.getMessage()));
    }

    Optional<Board> findByIdAndDeletedAtIsNull(Long boardId);

    List<Board> findAllByDeletedAtIsNull();

    List<Board> findAllByDeletedAtIsNullAndBoardType(BoardType boardType);
}
