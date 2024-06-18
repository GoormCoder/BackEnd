package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Battle;
import goormcoder.webide.domain.BattleWait;
import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.Question;
import goormcoder.webide.dto.request.BattleWaitCreateDto;
import goormcoder.webide.dto.response.BattleInfoDto;
import goormcoder.webide.dto.response.BattleWaitFindDto;
import goormcoder.webide.dto.response.BattleWaitSimpleDto;
import goormcoder.webide.exception.ConflictException;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.BattleRepository;
import goormcoder.webide.repository.BattleWaitRepository;
import goormcoder.webide.repository.MemberRepository;
import goormcoder.webide.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleWaitRepository battleWaitRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final BattleRepository battleRepository;

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

    //대기방 조회
    @Transactional(readOnly = true)
    public BattleWaitFindDto findBattleWait(String memberLoginId, Long roomId) {
        BattleWait battleWait = findByIdOrThrow(roomId);

        Member member = memberRepository.findByLoginIdOrThrow(memberLoginId);

        validateMemberAccessToBattleWait(member, battleWait);

        return BattleWaitFindDto.of(battleWait.getId(), battleWait.getGivenMember(), battleWait.getReceivedMember(), battleWait.isFull());
    }

    //배틀 시작
    @Transactional
    public BattleInfoDto startBattle(String memberLoginId, Long roomId) {
        BattleWait battleWait = findByIdOrThrow(roomId);

        Member member = memberRepository.findByLoginIdOrThrow(memberLoginId);

        validateMemberAccessToBattleWait(member, battleWait);

        //난이도에 맞는 것들 중 랜덤으로 하나
        List<Question> questions = questionRepository.findByLevel(battleWait.getLevel());

        if (questions.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessages.QUESTION_NOT_FOUND.getMessage());
        }

        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());
        Question randomQuestion = questions.get(randomIndex);

        Battle battle = Battle.of(battleWait.getGivenMember(), battleWait.getReceivedMember(), randomQuestion);
        battleRepository.save(battle);

        battleWaitRepository.delete(battleWait);

        return BattleInfoDto.of(battle, battle.getQuestion());
    }

    public BattleWait findByIdOrThrow(Long roomId) {
        return battleWaitRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.BATTLE_WAIT_NOT_FOUND.getMessage()));
    }

    private void validateMemberAccessToBattleWait(Member member, BattleWait battleWait) {
        if (!member.getId().equals(battleWait.getGivenMember().getId()) &&
                (battleWait.getReceivedMember() == null || !member.getId().equals(battleWait.getReceivedMember().getId()))) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_ACCESS);
        }
    }
}
