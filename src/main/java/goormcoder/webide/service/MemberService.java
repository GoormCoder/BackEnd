package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Member;
import goormcoder.webide.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
    }
}
