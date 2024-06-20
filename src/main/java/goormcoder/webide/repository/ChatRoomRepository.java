package goormcoder.webide.repository;

import goormcoder.webide.domain.ChatRoom;
import goormcoder.webide.domain.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByUniqueKey(String uniqueKey);

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.chatRoomMembers crm JOIN crm.member m WHERE m.loginId = :loginId AND crm.deletedAt IS NULL")
    List<ChatRoom> findChatRoomsByMemberLoginId(@Param("loginId") String loginId);

    @Query("SELECT m.name FROM Member m JOIN ChatRoomMember crm ON m.id = crm.member.id WHERE crm.chatRoom.id = :chatRoomId AND m.loginId <> :currentUserLoginId")
    String findChatRoomOtherMemberUsername(@Param("chatRoomId") Long chatRoomId, @Param("currentUserLoginId") String currentUserLoginId);

}
