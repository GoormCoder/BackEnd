package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Solve;
import goormcoder.webide.dto.response.SolveSummaryDto;
import goormcoder.webide.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import goormcoder.webide.domain.enumeration.MemberRole;
import goormcoder.webide.dto.request.MemberJoinDto;
import goormcoder.webide.domain.enumeration.Gender;
import goormcoder.webide.dto.response.MemberFindAllDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
    }

    @Transactional
    public List<MemberFindAllDto> getAllMembersByLoginIdContaining(String keyword) {
        return MemberFindAllDto.listOf(memberRepository.findAllByLoginIdContaining(keyword));
    }

    public Member registerMember(MemberJoinDto memberJoinDto) {
        if (isLoginIdDuplicated(memberJoinDto.loginId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(memberJoinDto.password());

        Member member = Member.of(
                memberJoinDto.loginId(),
                encodedPassword,
                memberJoinDto.nick(), // nick 필드가 없으므로 loginId를 대체 사용
                memberJoinDto.name(),
                MemberRole.ROLE_USER, // 기본 역할 설정
                LocalDate.now(), // 예시로 현재 날짜 사용
                memberJoinDto.email(),
                "",
                Gender.valueOf(memberJoinDto.gender().toUpperCase()) // Gender enum으로 변환
        );

        return memberRepository.save(member);
    }

    public boolean isLoginIdDuplicated(String loginId) {
        return memberRepository.findByLoginId(loginId).isPresent();
    }

    public List<SolveSummaryDto> findSolvesByLoginId(String loginId) {
        return SolveSummaryDto.listOf(
                this.findByLoginId(loginId).getSolves()
        );
    }

    @Transactional(readOnly = true)
    public String findLoginIdByEmailAndName(String email, String name) {
        return memberRepository.findByEmailAndName(email, name)
                .map(Member::getLoginId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 존재하지 않습니다."));
    }

    @Transactional
    public boolean verifyEmailAndLoginId(String email, String loginId) {
        Optional<Member> member = memberRepository.findByEmailAndLoginId(email, loginId);
        return member.isPresent();
    }

    public void resetPassword(String userId, String newPassword) {
        Member member = memberRepository.findByLoginId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 회원을 찾을 수 없습니다."));

        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

}
