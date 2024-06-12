package goormcoder.webide.service;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.request.BoardCreateDto;
import goormcoder.webide.dto.response.BoardFindAllDto;
import goormcoder.webide.dto.response.BoardFindDto;
import goormcoder.webide.exception.NotFoundException;
import goormcoder.webide.repository.BoardRepository;
import goormcoder.webide.repository.MemberRepository;
import goormcoder.webide.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final QuestionRepository questionRepository;

    // 게시글 생성
    @Transactional
    public void createBoard(BoardCreateDto boardCreateDto) {
        Member member = memberRepository.findByIdOrThrow(boardCreateDto.memberId());

        Question question = null;
        if(boardCreateDto.questionNum() != null) {
            question = questionRepository.findByQuestionNum(boardCreateDto.questionNum())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.QUESTION_NOT_FOUND));
        }

        boardRepository.save(Board.of(boardCreateDto, member, question));
    }

    //게시글 조회
    @Transactional(readOnly = true)
    public List<BoardFindAllDto> getAllBoards() {
        return BoardFindAllDto.listOf(boardRepository.findAll());
    }

    //게시글 열람
    @Transactional(readOnly = true)
    public BoardFindDto getBoard(Long boardId) {
        return BoardFindDto.from(boardRepository.findByIdOrThrow(boardId));
    }
}
