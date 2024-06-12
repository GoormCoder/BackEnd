package goormcoder.webide.repository;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Member;
import goormcoder.webide.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    default Board findByIdOrThrow(final Long boardId) {
        return findById(boardId).orElseThrow(() -> new NotFoundException(ErrorMessage.BOARD_NOT_FOUND));
    }
}
