package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.request.BoardCreateDto;
import goormcoder.webide.dto.request.BoardUpdateDto;
import goormcoder.webide.dto.response.BoardFindAllDto;
import goormcoder.webide.dto.response.BoardFindDto;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.BoardRepository;
import goormcoder.webide.repository.MemberRepository;
import goormcoder.webide.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final QuestionRepository questionRepository;

    // 게시글 생성
    @Transactional
    public void createBoard(String loginId, BoardCreateDto boardCreateDto) {
        Member member = memberRepository.findByLoginIdOrThrow(loginId);

        Question question = null;
        if(boardCreateDto.questionId() != null) {
            question = questionRepository.findById(boardCreateDto.questionId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.QUESTION_NOT_FOUND.getMessage()));
        }

        boardRepository.save(Board.of(boardCreateDto.boardType(), boardCreateDto.title(), boardCreateDto.content(), member, question));
    }

    //게시글 조회
    @Transactional(readOnly = true)
    public List<BoardFindAllDto> getAllBoards() {
        return BoardFindAllDto.listOf(boardRepository.findAllByDeletedAtIsNull());
    }

    //게시글 열람
    @Transactional(readOnly = true)
    public BoardFindDto getBoard(Long boardId) {
        return BoardFindDto.from(boardRepository.findByIdOrThrow(boardId));
    }

    //게시글 수정
    @Transactional
    public void updateBoard(String loginId, Long boardId, BoardUpdateDto boardUpdateDto) {
        Board board = boardRepository.findByIdOrThrow(boardId);
        Member member = memberRepository.findByLoginIdOrThrow(loginId);

        if(!member.getId().equals(board.getMember().getId())) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_ACCESS);
        }
        board.patch(boardUpdateDto.title(), boardUpdateDto.content());
    }

    //게시글 삭제
    @Transactional
    public void deleteBoard(String loginId, Long boardId) {
        Board board = boardRepository.findByIdOrThrow(boardId);
        Member member = memberRepository.findByLoginIdOrThrow(loginId);

        if(!member.getId().equals(board.getMember().getId())) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_ACCESS);
        }
        board.markAsDeleted();
    }
}
