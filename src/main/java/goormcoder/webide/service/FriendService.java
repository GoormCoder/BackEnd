package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.*;
import goormcoder.webide.dto.request.FriendCreateDto;
import goormcoder.webide.dto.request.FriendRequestCreatDto;
import goormcoder.webide.dto.response.FriendFindAllDto;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.FriendRepository;
import goormcoder.webide.repository.FriendRequestRepository;
import goormcoder.webide.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    // 친구요청
    @Transactional
    public boolean requestFriend(String loginId, FriendRequestCreatDto friendRequestCreateDto){
        Member requestMember = memberRepository.findByLoginIdOrThrow(loginId);
        Member receivedMember = memberRepository.findByLoginIdOrThrow(friendRequestCreateDto.receivedLoginId());
        List<FriendRequest> requestDup = friendRequestRepository.findByRequestIdAndReceivedId(requestMember, receivedMember);
        List<FriendRequest> receivedDup = friendRequestRepository.findByRequestIdAndReceivedId(receivedMember, requestMember);

        if(requestDup.size() != 0 || receivedDup.size() != 0) {
            return false;
        }
        friendRequestRepository.save(FriendRequest.of(requestMember, receivedMember));
        return true;
    }

    // 친구 추가
    @Transactional
    public void createFriend(String loginId, FriendCreateDto friendCreateDto) {
        updateFriendRequestResult(loginId, friendCreateDto.requestId());
        Member member = memberRepository.findByLoginIdOrThrow(loginId);
        Member friend = memberRepository.findByLoginIdOrThrow(friendCreateDto.friendLoginId());
        friendRepository.save(Friend.of(member, friend));
        friendRepository.save(Friend.of(friend, member));
    }

    // 요청 상태 업데이트
    @Transactional
    public void updateFriendRequestResult(String loginId, Long requestId) {
        FriendRequest request = friendRequestRepository.findByIdOrThrow(requestId);
        Member member = memberRepository.findByLoginIdOrThrow(loginId);

        if(!member.getId().equals(request.getReceivedId().getId())) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_FRIEND_REQUEST_ACCESS);
        }
        request.patch();
    }

    //친구 조회
    @Transactional(readOnly = true)
    public List<FriendFindAllDto> getAllFriends(String loginId) {
        Member member = memberRepository.findByLoginIdOrThrow(loginId);
        return FriendFindAllDto.listOf(friendRepository.findAllByMemberId(member));
    }
}
