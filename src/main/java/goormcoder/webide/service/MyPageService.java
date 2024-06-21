package goormcoder.webide.service;

import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.MyPageDto;
import goormcoder.webide.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final MemberRepository memberRepository;

    public MyPageDto getMyPageByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 로그인 아이디입니다."));

        return MyPageDto.builder()
                .loginId(member.getLoginId())
                .nick(member.getNick())
                .name(member.getName())
                .birth(member.getBirth())
                .email(member.getEmail())
                .info(member.getInfo())
                .gender(member.getGender())
                .praiseScore(member.getPraiseScore())
                .battleScore(member.getBattleScore())
                .build();
    }

    @Transactional
    public void updateNick(String loginId, String newNick) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 로그인 아이디입니다."));
        member.setNick(newNick);
    }

    @Transactional
    public void updateBirth(String loginId, LocalDate newBirth) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 로그인 아이디입니다."));
        member.setBirth(newBirth);
    }

    @Transactional
    public void updateInfo(String loginId, String newInfo) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 로그인 아이디입니다."));
        member.setInfo(newInfo);
    }
}
