package goormcoder.webide.repository;

import goormcoder.webide.domain.Friend;
import goormcoder.webide.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findAllByMemberId(Member memberId);
    List<Friend> findByMemberIdAndFriendId(Member memberId, Member friendId);

}
