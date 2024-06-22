package goormcoder.webide.repository;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member findByLoginIdOrThrow(final String loginId) {
        return findByLoginId(loginId).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
    }

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByEmailAndName(String email, String name);

    Optional<Member> findByEmailAndLoginId(String email, String loginId);


    default Member findByIdOrThrow(final Long memberId) {
        return findById(memberId).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
    }

    @Query("SELECT m FROM Member m WHERE m.loginId LIKE %:keyword%")
    List<Member> findAllByLoginIdContaining(@Param("keyword") String keyword);

    List<Member> findAllByOrderByPraiseScoreDesc();

    List<Member> findAllByOrderByBattleScoreDesc();
}