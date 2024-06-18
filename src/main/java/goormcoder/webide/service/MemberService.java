package goormcoder.webide.service;

import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.response.FriendFindAllDto;
import goormcoder.webide.dto.response.MemberFindAllDto;
import goormcoder.webide.dto.response.MemberFindDto;
import goormcoder.webide.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public List<MemberFindAllDto> getAllMembersByLoginIdContaining(String keyword) {
        return MemberFindAllDto.listOf(memberRepository.findAllByLoginIdContaining(keyword));
    }
}
