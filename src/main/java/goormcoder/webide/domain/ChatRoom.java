package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Table(name = "t_chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "chat_room_unique_key", unique = true)
    private String uniqueKey;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST)
    private final Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST)
    private final List<ChatMessage> chatMessages = new ArrayList<>();

    @Builder
    private ChatRoom(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public static ChatRoom of(String uniqueKey) {
        return ChatRoom.builder()
                .uniqueKey(uniqueKey)
                .build();
    }

    public void addChatRoomMember(ChatRoomMember chatRoomMember) {
        chatRoomMembers.add(chatRoomMember);
    }

    public void addChatMessage(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
    }

    public void deleteUniqueKey() {
        this.uniqueKey = null;
    }

    @Override
    public void markAsDeleted() {
        super.markAsDeleted();
        chatMessages.forEach(ChatMessage::markAsDeleted);
    }
}
