package goormcoder.webide.service;

import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Comment;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.CommentCreateDto;
import goormcoder.webide.dto.request.CommentUpdateDto;
import goormcoder.webide.dto.response.CommentFindAllDto;
import goormcoder.webide.repository.BoardRepository;
import goormcoder.webide.repository.CommentRepository;
import goormcoder.webide.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    //댓글 생성
    public void createComment(Long boardId, CommentCreateDto commentCreateDto) {
        Board board = boardRepository.findByIdOrThrow(boardId);
        Member member = memberRepository.findByIdOrThrow(commentCreateDto.memberId()); //나중에 수정해야함
        commentRepository.save(Comment.of(commentCreateDto, board, member));
    }

    //댓글 조회
    public List<CommentFindAllDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return CommentFindAllDto.listOf(comments);
    }

    //댓글 수정
    public void updateComment(Long commentId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findByIdOrThrow(commentId);

        //jwt 이후 추가 예정
//        if(!memberId.equals(comment.getMember().getId())) {
//            throw new ForbiddenException(ErrorMessage.FORBIDDEN_MEMBER_ACCESS);
//        }
        comment.patch(commentUpdateDto);
    }
}
