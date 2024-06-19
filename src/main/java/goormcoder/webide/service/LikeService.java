package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Like;
import goormcoder.webide.domain.Member;
import goormcoder.webide.exception.ConflictException;
import goormcoder.webide.repository.BoardRepository;
import goormcoder.webide.repository.LikeRepository;
import goormcoder.webide.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    //좋아요 생성
    @Transactional
    public void createLike(String loginId, Long boardId) {
        Member member = memberRepository.findByLoginIdOrThrow(loginId);
        Board board = boardRepository.findByIdOrThrow(boardId);

        likeRepository.findByMemberAndBoard(member, board)
                .ifPresent(b -> {
                    throw new ConflictException(ErrorMessages.LIKE_CONFLICT);
                });

        likeRepository.save(Like.of(member, board));

        board.addLikeCount();

        Member boardMember = board.getMember();
        boardMember.incrementPraiseScore();
    }

    //좋아요 삭제
    @Transactional
    public void deleteLike(String loginId, Long boardId) {
        Member member = memberRepository.findByLoginIdOrThrow(loginId);
        Board board = boardRepository.findByIdOrThrow(boardId);

        Like like = likeRepository.findByMemberAndBoard(member, board)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.LIKE_NOT_FOUND.getMessage()));

        likeRepository.delete(like);

        board.removeLikeCount();

        Member boardMember = board.getMember();
        boardMember.decrementPraiseScore();
    }
}
