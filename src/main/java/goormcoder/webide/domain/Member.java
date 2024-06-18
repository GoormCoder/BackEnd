package goormcoder.webide.domain;

import goormcoder.webide.domain.enumeration.Gender;
import goormcoder.webide.domain.enumeration.MemberRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "t_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "member_pw", nullable = false)
    private String password;

    @Column(name = "member_nick", nullable = false)
    private String nick;

    @Column(name = "member_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole role;

    @Column(name = "member_birth", nullable = false)
    private LocalDate birth;

    @Column(name = "member_email", unique = true)
    private String email;

    @Column(name = "member_info")
    private String info;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_gender", nullable = false)
    private Gender gender;

    @Column(name = "member_praise", nullable = false)
    private int praiseScore = 0;

    @Column(name = "battle_score", nullable = false)
    private int battleScore = 1000;

    @Builder
    private Member(String loginId, String password, String nick, String name, MemberRole role, LocalDate birth, String email, String info, Gender gender, int praiseScore, int battleScore) {
        this.loginId = loginId;
        this.password = password;
        this.nick = nick;
        this.name = name;
        this.role = role;
        this.birth = birth;
        this.email = email;
        this.info = info;
        this.gender = gender;
        this.praiseScore = praiseScore;
        this.battleScore = battleScore;
    }

    public static Member of(String loginId, String password, String nick, String name, MemberRole role,
                            LocalDate birth, String email, String info, Gender gender) {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .nick(nick)
                .name(name)
                .role(role)
                .birth(birth)
                .email(email)
                .info(info)
                .gender(gender)
                .praiseScore(0)
                .battleScore(1000)
                .build();
    }

}
