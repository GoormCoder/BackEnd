package goormcoder.webide.repository;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Member;
import goormcoder.webide.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);

    default Member findByIdOrThrow(final Long memberId) {
        return findById(memberId).orElseThrow(() -> new EntityNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
    }
  
}