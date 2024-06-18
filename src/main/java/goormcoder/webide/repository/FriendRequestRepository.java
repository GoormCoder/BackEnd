package goormcoder.webide.repository;

import goormcoder.webide.domain.Friend;
import goormcoder.webide.domain.FriendRequest;
import goormcoder.webide.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByRequestIdAndReceivedId(Member requestId, Member receivedId);
}
