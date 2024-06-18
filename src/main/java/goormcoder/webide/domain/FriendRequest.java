package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "t_friend_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_request_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_member_id", referencedColumnName = "member_id")
    private Member requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_member_id", referencedColumnName = "member_id")
    private Member receivedId;

    @Column(name = "request_result", nullable = false)
    private Character requestResult;

    @Builder
    private FriendRequest(Member requestId, Member receivedId) {
        this.requestId = requestId;
        this.receivedId = receivedId;
    }

    public static FriendRequest of(Member requestId, Member receivedId) {
        return FriendRequest.builder()
                .requestId(requestId)
                .receivedId(receivedId)
                .build();
    }

    @PrePersist
    protected void onPrePersist() {
        if (requestResult == null) {
            requestResult = 'F';
        }
    }
}
