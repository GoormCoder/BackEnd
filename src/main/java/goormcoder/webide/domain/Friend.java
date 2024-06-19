package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "t_friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_member_id", referencedColumnName = "member_id")
    private Member friendId;

    @Builder
    private Friend(Member memberId, Member friendId) {
        this.memberId = memberId;
        this.friendId = friendId;
    }

    public static Friend of(Member memberId, Member friendId) {
        return Friend.builder()
                .memberId(memberId)
                .friendId(friendId)
                .build();
    }
}
