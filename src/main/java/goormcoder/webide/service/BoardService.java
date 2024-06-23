package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.enumeration.BoardType;
import goormcoder.webide.dto.request.BoardCreateDto;
import goormcoder.webide.dto.request.BoardUpdateDto;
import goormcoder.webide.dto.response.BoardFindAllDto;
import goormcoder.webide.dto.response.BoardFindDto;
import goormcoder.webide.dto.response.BoardSummaryDto;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.BoardRepository;
import goormcoder.webide.repository.MemberRepository;
import goormcoder.webide.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<BoardSummaryDto> getAllBoards(Pageable pageable) {
        return boardRepository.findAllByDeletedAtIsNull(
                    PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort())
                )
                .map(BoardSummaryDto::of);
    }

    //게시글 유형별 조회
    @Transactional(readOnly = true)
    public Page<BoardSummaryDto> getBoardsByType(BoardType boardType, Pageable pageable) {
        return boardRepository
                .findAllByDeletedAtIsNullAndBoardType(
                        boardType,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort())
                )
                .map(BoardSummaryDto::of);
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
