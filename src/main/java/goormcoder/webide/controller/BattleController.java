package goormcoder.webide.controller;

import goormcoder.webide.dto.request.BattleWaitCreateDto;
import goormcoder.webide.dto.request.SolveCreateDto;
import goormcoder.webide.dto.response.*;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.BattleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Battle", description = "배틀 관련 API")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final PrincipalHandler principalHandler;

    //대기방 참가
    @PostMapping("/battles/wait")
    @Operation(summary = "배틀 대기방 등록", description = "랜덤 매칭 버튼을 누르면 배틀 대기방에 참가합니다.")
    public ResponseEntity<BattleWaitSimpleDto> createBattleWait(@Valid @RequestBody BattleWaitCreateDto battleWaitCreateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(battleService.createBattleWait(principalHandler.getMemberLoginId(), battleWaitCreateDto));
    }

    //대기방 조회
    @GetMapping("/battles/wait/{roomId}")
    @Operation(summary = "배틀 대기방 조회", description = "배틀 대기방 회원 목록 및 대결 준비 여부를 조회합니다.")
    public ResponseEntity<BattleWaitFindDto> findBattleWait(@PathVariable Long roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(battleService.findBattleWait(principalHandler.getMemberLoginId(), roomId));
    }

    //배틀 시작
    @PostMapping("/battles/start/{roomId}")
    @Operation(summary = "배틀 시작", description = "대결할 회원이 다 모였으면 대결을 시작합니다.")
    public ResponseEntity<BattleInfoDto> startBattle(@PathVariable Long roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(battleService.startBattle(principalHandler.getMemberLoginId(), roomId));
    }

    //풀이 제출 및 결과 확인
    @PostMapping("/battles/submit/{battleId}/{questionId}")
    @Operation(summary = "배틀 풀이 제출 및 결과 확인", description = "배틀 풀이를 제출하고, 결과(승패 여부)를 확인합니다.")
    public ResponseEntity<BattleSubmitDto> submitSolution(@PathVariable Long battleId, @PathVariable Long questionId, @RequestBody SolveCreateDto solveCreateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(battleService.submitSolution(principalHandler.getMemberLoginId(), battleId, questionId, solveCreateDto));
    }

    //사용자 배틀 전적 조회
    @GetMapping("/battles/record")
    @Operation(summary = "사용자 배틀 전적 조회", description = "사용자의 최근 배틀 전적을 조회합니다")
    public ResponseEntity<BattleRecordFindAllDto> findBattleRecord() {
        return ResponseEntity.status(HttpStatus.OK).body(battleService.findBattleRecord(principalHandler.getMemberLoginId()));
    }

}
