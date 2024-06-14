package goormcoder.webide.service;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Board;
import goormcoder.webide.domain.Like;
import goormcoder.webide.domain.Member;
import goormcoder.webide.exception.ConflictException;
import goormcoder.webide.exception.NotFoundException;
import goormcoder.webide.repository.BoardRepository;
import goormcoder.webide.repository.LikeRepository;
import goormcoder.webide.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    //좋아요 생성
    public void createLike(Long memberId, Long boardId) {
        Member member = memberRepository.findByIdOrThrow(memberId);
        Board board = boardRepository.findByIdOrThrow(boardId);

        likeRepository.findByMemberAndBoard(member, board)
                .ifPresent(b -> {
                    throw new ConflictException(ErrorMessage.LIKE_CONFLICT);
                });

        board.addLikeCount();
        likeRepository.save(Like.of(member, board));
    }

    //좋아요 삭제
    public void deleteLike(Long memberId, Long boardId) {
        Member member = memberRepository.findByIdOrThrow(memberId);
        Board board = boardRepository.findByIdOrThrow(boardId);

        Like like = likeRepository.findByMemberAndBoard(member, board)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.LIKE_NOT_FOUND));

        board.removeLikeCount();
        likeRepository.delete(like);
    }
}
