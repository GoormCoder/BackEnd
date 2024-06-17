package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "t_battle_solve")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BattleSolve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_solve_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_id")
    private Battle battle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solve_id")
    private Solve solve;

    public static BattleSolve of(final Battle battle, final Solve solve) {
        return BattleSolve.builder()
                .battle(battle)
                .solve(solve)
                .build();
    }

    @Builder
    private BattleSolve(final Battle battle, final Solve solve) {
        this.battle = battle;
        this.solve = solve;
    }
}
