package goormcoder.webide.service;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Comment;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.CommentCreateDto;
import goormcoder.webide.dto.request.CommentUpdateDto;
import goormcoder.webide.dto.response.CommentFindAllDto;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.repository.BoardRepository;
import goormcoder.webide.repository.CommentRepository;
import goormcoder.webide.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    //댓글 생성
    @Transactional
    public void createComment(Long boardId, String loginId, CommentCreateDto commentCreateDto) {
        Board board = boardRepository.findByIdOrThrow(boardId);
        Member member = memberRepository.findByLoginIdOrThrow(loginId);
        commentRepository.save(Comment.of(commentCreateDto.content(), board, member));
    }

    //댓글 조회
    @Transactional(readOnly = true)
    public List<CommentFindAllDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return CommentFindAllDto.listOf(comments);
    }

    //댓글 수정
    @Transactional
    public void updateComment(String loginId, Long commentId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findByIdOrThrow(commentId);
        Member member = memberRepository.findByLoginIdOrThrow(loginId);

        if(!member.getId().equals(comment.getMember().getId())) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_BOARD_ACCESS);
        }
        comment.patch(commentUpdateDto.content());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(String loginId, Long commentId) {
        Comment comment = commentRepository.findByIdOrThrow(commentId);
        Member member = memberRepository.findByLoginIdOrThrow(loginId);

        if(!member.getId().equals(comment.getMember().getId())) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_BOARD_ACCESS);
        }
        comment.markAsDeleted();
    }
}
