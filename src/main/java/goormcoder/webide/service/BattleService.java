package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.BattleWait;
import goormcoder.webide.domain.Member;
import goormcoder.webide.dto.request.BattleWaitCreateDto;
import goormcoder.webide.dto.response.BattleWaitFindDto;
import goormcoder.webide.dto.response.BattleWaitSimpleDto;
import goormcoder.webide.exception.ConflictException;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.BattleWaitRepository;
import goormcoder.webide.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleWaitRepository battleWaitRepository;
    private final MemberRepository memberRepository;

    //대기방 등록
    @Transactional
    public BattleWaitSimpleDto createBattleWait(String memberLoginId, BattleWaitCreateDto battleWaitCreateDto) {
        List<BattleWait> availableRooms = battleWaitRepository.findByLevelAndLanguageAndIsFullFalseOrderByCreatedAtAsc(
                battleWaitCreateDto.level(),
                battleWaitCreateDto.language()
        );

        if (!availableRooms.isEmpty()) {
            BattleWait room = availableRooms.get(0);

            if (room.getGivenMember().getLoginId().equals(memberLoginId)) {
                throw new ConflictException(ErrorMessages.BATTLE_MEMBER_CONFLICT);
            }

            Member receivedMember = memberRepository.findByLoginIdOrThrow(memberLoginId);
            room.joinMember(receivedMember);
            return BattleWaitSimpleDto.of(room.getId(), room.isFull());
        } else {
            Member givenMember = memberRepository.findByLoginIdOrThrow(memberLoginId);
            BattleWait newRoom = BattleWait.of(battleWaitCreateDto.level(), battleWaitCreateDto.language(), givenMember);
            battleWaitRepository.save(newRoom);
            return BattleWaitSimpleDto.of(newRoom.getId(), newRoom.isFull());
        }
    }

    @Transactional(readOnly = true)
    public BattleWaitFindDto findBattleWait(String memberLoginId, Long roomId) {
        BattleWait battleWait = battleWaitRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.BATTLE_WAIT_NOT_FOUND.getMessage()));

        Member member = memberRepository.findByLoginIdOrThrow(memberLoginId);

        if (!member.getId().equals(battleWait.getGivenMember().getId()) &&
                (battleWait.getReceivedMember() == null || !member.getId().equals(battleWait.getReceivedMember().getId()))) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_ACCESS);
        }

        return BattleWaitFindDto.of(battleWait.getId(), battleWait.getGivenMember(), battleWait.getReceivedMember(), battleWait.isFull());
    }
}
