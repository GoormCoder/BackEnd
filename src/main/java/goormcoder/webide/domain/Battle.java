package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "t_battle")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Battle extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_given_member_id")
    private Member givenMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_received_member_id")
    private Member receivedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_winner_id")
    private Member winner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Question question;

    public static Battle of(final Member givenMember, final Member receivedMember, final Question question) {
        return Battle.builder()
                .givenMember(givenMember)
                .receivedMember(receivedMember)
                .winner(null)
                .question(question)
                .build();
    }

    @Builder
    private Battle(final LocalDateTime battledAt, final Member givenMember, final Member receivedMember, final Member winner, final Question question) {
        this.givenMember = givenMember;
        this.receivedMember = receivedMember;
        this.winner = winner;
        this.question = question;
    }
}
