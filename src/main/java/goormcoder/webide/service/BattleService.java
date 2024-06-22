package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.*;
import goormcoder.webide.domain.enumeration.SolveResult;
import goormcoder.webide.dto.request.BattleWaitCreateDto;
import goormcoder.webide.dto.request.SolveCreateDto;
import goormcoder.webide.dto.response.*;
import goormcoder.webide.exception.ConflictException;
import goormcoder.webide.exception.ForbiddenException;
import goormcoder.webide.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    private final QuestionService questionService;
    private final MemberService memberService;
    private final SolveService solveService;
    private final BattleSolveRepository battleSolveRepository;

    //대기방 등록
    @Transactional
    public BattleWaitSimpleDto createBattleWait(String memberLoginId, BattleWaitCreateDto battleWaitCreateDto) {
        List<BattleWait> availableRooms = battleWaitRepository.findByLevelAndLanguage(
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

            //난이도에 맞는 것들 중 랜덤으로 하나
            List<Question> questions = questionRepository.findByLevel(battleWaitCreateDto.level());
            if (questions.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessages.QUESTION_NOT_FOUND.getMessage());
            }
            Random random = new Random();
            Question selectedQuestion = questions.get(random.nextInt(questions.size()));

            BattleWait newRoom = BattleWait.of(battleWaitCreateDto.level(), battleWaitCreateDto.language(), givenMember, selectedQuestion);
            battleWaitRepository.save(newRoom);
            return BattleWaitSimpleDto.of(newRoom.getId(), newRoom.isFull());
        }


    }

    //대기방 조회
    @Transactional(readOnly = true)
    public BattleWaitFindDto findBattleWait(String memberLoginId, Long roomId) {
        BattleWait battleWait = findByRoomIdOrThrow(roomId);

        Member member = memberRepository.findByLoginIdOrThrow(memberLoginId);

        validateMemberAccessToBattleWait(member, battleWait);

        return BattleWaitFindDto.of(battleWait.getId(), battleWait.getGivenMember(), battleWait.getReceivedMember(), battleWait.isFull());
    }

    //배틀 취소 및 대기방 삭제
    @Transactional
    public void cancelBattleWait(String memberLoginId, Long roomId) {
        Member member = memberRepository.findByLoginIdOrThrow(memberLoginId);
        BattleWait battleWait = findByRoomIdOrThrow(roomId);

        //사용자 권한 검증
        validateMemberAccessToBattleWait(member, battleWait);

        //대기방 상태 검증
        if (battleWait.isFull()) {
            throw new ConflictException(ErrorMessages.BATTLE_CANCEL_CONFLICT);
        }

        battleWait.markAsDeleted();
    }

    //배틀 시작
    @Transactional
    public BattleInfoDto startBattle(String memberLoginId, Long roomId) {
        BattleWait battleWait = findByRoomIdOrThrow(roomId);

        Member member = memberRepository.findByLoginIdOrThrow(memberLoginId);
        validateMemberAccessToBattleWait(member, battleWait);

        // 이미 시작된 배틀이 있는지 확인
        if (battleWait.getBattle() != null) {
            battleWait.markAsDeleted();
            return BattleInfoDto.of(battleWait.getBattle(), battleWait.getQuestion());
        }

        Question question = battleWait.getQuestion();

        Battle battle = Battle.of(battleWait.getGivenMember(), battleWait.getReceivedMember(), question);
        battleRepository.save(battle);
        battleWait.assignBattle(battle);

        return BattleInfoDto.of(battle, question);
    }

    //풀이 제출 및 결과 확인
    @Transactional
    public BattleSubmitDto submitSolution(String memberLoginId, Long battleId, Long questionId, SolveCreateDto solveCreateDto) {
        Question question = questionService.findById(questionId);
        Member member = memberService.findByLoginId(memberLoginId);

        //풀이 생성 로직 호출
        Solve solve = solveService.create(question, member, solveCreateDto);

        Battle battle = findByIdOrThrow(battleId);

        //대결 풀이 연결
        battleSolveRepository.save(BattleSolve.of(battle, solve));

        //정답 확인
        if (solve.getSolveResult() == SolveResult.CORRECT) {
            // 대결 결과 저장 및 승자 판별
            if (battle.getWinner() == null) {
                battle.saveWinner(solve.getMember());
                member.incrementBattleScore();
                return BattleSubmitDto.of(solve, "배틀에서 승리했습니다!");
            } else {
                return BattleSubmitDto.of(solve, "배틀에서 패배했습니다. 상대방이 먼저 문제를 맞췄습니다.");
            }
        } else {
            return BattleSubmitDto.of(solve, null);
        }
    }

    //사용자 전적 조회
    @Transactional(readOnly = true)
    public BattleRecordFindAllDto findBattleRecord(String memberLoginId) {
        Member member = memberRepository.findByLoginIdOrThrow(memberLoginId);

        int totalCount = battleRepository.countByMember(member);
        int winCount = battleRepository.countByWinner(member);
        int loseCount = totalCount - winCount;

        double winRate = (totalCount > 0) ? ((double) winCount / totalCount) * 100 : 0;

        String totalResult = winCount + "승 " + loseCount + "패";
        String winRateStr = String.format("%.2f", winRate) + "% 승률";

        List<Battle> battles = battleRepository.findByMember(member, PageRequest.of(0, 5));

        return BattleRecordFindAllDto.of(member, battles, totalResult, winRateStr);
    }

    public BattleWait findByRoomIdOrThrow(Long roomId) {
        return battleWaitRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.BATTLE_WAIT_NOT_FOUND.getMessage()));
    }

    private void validateMemberAccessToBattleWait(Member member, BattleWait battleWait) {
        if (!member.getId().equals(battleWait.getGivenMember().getId()) &&
                (battleWait.getReceivedMember() == null || !member.getId().equals(battleWait.getReceivedMember().getId()))) {
            throw new ForbiddenException(ErrorMessages.FORBIDDEN_ACCESS);
        }
    }

    public Battle findByIdOrThrow(Long battleId) {
        return battleRepository.findById(battleId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.BATTLE_NOT_FOUND.getMessage()));
    }
}
