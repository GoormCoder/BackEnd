package goormcoder.webide.service;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.*;
import goormcoder.webide.dto.request.BoardCreateDto;
import goormcoder.webide.dto.request.BoardUpdateDto;
import goormcoder.webide.dto.request.CommentCreateDto;
import goormcoder.webide.dto.request.FriendCreateDto;
import goormcoder.webide.dto.response.BoardFindAllDto;
import goormcoder.webide.dto.response.BoardFindDto;
import goormcoder.webide.dto.response.FriendFindAllDto;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.BoardRepository;
import goormcoder.webide.repository.FriendRepository;
import goormcoder.webide.repository.MemberRepository;
import goormcoder.webide.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    // 친구 추가
    @Transactional
    public boolean createFriend(String loginId, FriendCreateDto friendCreateDto) {
        Member member = memberRepository.findByLoginIdOrThrow(loginId);
        Member friend = memberRepository.findByLoginIdOrThrow(friendCreateDto.friendLoginId());
        List<Friend> dup = friendRepository.findByMemberIdAndFriendId(member, friend);

        if(dup.size() == 0) {
            friendRepository.save(Friend.of(member, friend));
            friendRepository.save(Friend.of(friend, member));
            return true;
        }
        return false;
    }

    //친구 조회
    @Transactional(readOnly = true)
    public List<FriendFindAllDto> getAllFriends(String loginId) {
        Member member = memberRepository.findByLoginIdOrThrow(loginId);
        return FriendFindAllDto.listOf(friendRepository.findAllByMemberId(member));
    }

//    //게시글 열람
//    @Transactional(readOnly = true)
//    public BoardFindDto getBoard(Long boardId) {
//        return BoardFindDto.from(boardRepository.findByIdOrThrow(boardId));
//    }
//
//    //게시글 수정
//    @Transactional
//    public void updateBoard(String loginId, Long boardId, BoardUpdateDto boardUpdateDto) {
//        Board board = boardRepository.findByIdOrThrow(boardId);
//        Member member = memberRepository.findByLoginIdOrThrow(loginId);
//
//        if(!member.getId().equals(board.getMember().getId())) {
//            throw new ForbiddenException(ErrorMessage.FORBIDDEN_COMMENT_ACCESS);
//        }
//        board.patch(boardUpdateDto.title(), boardUpdateDto.content());
//    }
//
//    //게시글 삭제
//    @Transactional
//    public void deleteBoard(String loginId, Long boardId) {
//        Board board = boardRepository.findByIdOrThrow(boardId);
//        Member member = memberRepository.findByLoginIdOrThrow(loginId);
//
//        if(!member.getId().equals(board.getMember().getId())) {
//            throw new ForbiddenException(ErrorMessage.FORBIDDEN_COMMENT_ACCESS);
//        }
//        board.markAsDeleted();
//    }
}
