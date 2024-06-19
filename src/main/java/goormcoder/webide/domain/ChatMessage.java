package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "t_chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Long id;

    @Column(name = "chat_msg", nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Builder
    private ChatMessage(String message, Member member, ChatRoom chatRoom) {
        this.message = message;
        this.member = member;
        this.chatRoom = chatRoom;
    }

    public static ChatMessage of(String message, Member member, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .message(message)
                .member(member)
                .chatRoom(chatRoom)
                .build();
    }

}
