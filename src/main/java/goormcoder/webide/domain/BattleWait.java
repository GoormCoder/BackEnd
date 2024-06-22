package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "t_battle_wait")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BattleWait extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_wait_id")
    private Long id;

    @Column(name = "battle_level", nullable = false)
    private Integer level;

    @Column(name = "battle_language", nullable = false)
    private String language;

    @Column(name = "battle_wait_is_full", nullable = false)
    private boolean isFull;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_given_member_id")
    private Member givenMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_received_member_id")
    private Member receivedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_quest")
    private Question question;

    @OneToOne(fetch = FetchType.LAZY)
    private Battle battle;

    public static BattleWait of(final Integer level, final String language, final Member givenMember,
                                final Question question) {
        return BattleWait.builder()
                .level(level)
                .language(language)
                .givenMember(givenMember)
                .receivedMember(null)
                .isFull(false)
                .question(question)
                .build();
    }

    @Builder
    private BattleWait(final Integer level, final String language, final Member givenMember, final Member receivedMember, final boolean isFull,
                       final Question question, final Battle battle) {
        this.level = level;
        this.language = language;
        this.givenMember = givenMember;
        this.receivedMember = receivedMember;
        this.isFull = isFull;
        this.question = question;
        this.battle = battle;
    }

    public void joinMember(final Member receivedMember) {
        if (this.isFull) {
            throw new IllegalStateException("Room is already full");
        }
        this.receivedMember = receivedMember;
        this.isFull = true;
    }

    public void assignBattle(Battle battle) {
        if(this.battle != null) {
            throw new IllegalStateException("Battle is already assigned");
        }
        this.battle = battle;
    }
}
