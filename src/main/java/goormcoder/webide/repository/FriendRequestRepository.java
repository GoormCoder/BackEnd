package goormcoder.webide.repository;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.FriendRequest;
import goormcoder.webide.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    default FriendRequest findByIdAndRequesterIdOrThrow(final Long requestId, final Member requester) {
        return findByIdAndRequestIdAndRequestResult(requestId, requester, 'F').orElseThrow(() -> new EntityNotFoundException(ErrorMessages.FRIEND_REQUEST_NOT_FOUND.getMessage()));
    }
    Optional<FriendRequest> findByIdAndRequestIdAndRequestResult(Long requestId, Member requester, Character requestResult);
    List<FriendRequest> findByRequestIdAndReceivedId(Member requester, Member receiver);
    List<FriendRequest> findAllByReceivedIdAndRequestResult(Member receiver, Character requestResult);
}
