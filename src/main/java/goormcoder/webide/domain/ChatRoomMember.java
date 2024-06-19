package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "t_chat_room_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_member_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Setter
    @Column(name = "read_at", nullable = false)
    private LocalDateTime readAt;

    @Setter
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private ChatRoomMember(Member member, ChatRoom chatRoom, LocalDateTime readAt) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.readAt = readAt;
    }

    public static ChatRoomMember of(Member member, ChatRoom chatRoom) {
        return ChatRoomMember.builder()
                .member(member)
                .chatRoom(chatRoom)
                .readAt(LocalDateTime.now())
                .build();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

}
