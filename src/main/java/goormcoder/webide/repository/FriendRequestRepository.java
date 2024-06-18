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
    default FriendRequest findByIdOrThrow(final Long requestId) {
        return findByIdAndRequestResult(requestId, 'F').orElseThrow(() -> new EntityNotFoundException(ErrorMessages.FRIEND_REQUEST_NOT_FOUND.getMessage()));
    }
    Optional<FriendRequest> findByIdAndRequestResult(Long requestId, Character requestResult);
    List<FriendRequest> findByRequestIdAndReceivedId(Member requestId, Member receivedId);
}
