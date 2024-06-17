package goormcoder.webide.controller;

import goormcoder.webide.dto.request.BattleWaitCreateDto;
import goormcoder.webide.dto.response.BattleWaitFindDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.BattleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Battle", description = "배틀 관련 API")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final PrincipalHandler principalHandler;

    //대기방 참가
    @PostMapping("/battles/wait")
    @Operation(summary = "배틀 대기방 등록", description = "랜덤 매칭 버튼을 누르면 배틀 대기방에 참가합니다.")
    public ResponseEntity<BattleWaitFindDto> createBattleWait(@Valid @RequestBody BattleWaitCreateDto battleWaitCreateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(battleService.createBattleWait(principalHandler.getMemberLoginId(), battleWaitCreateDto));
    }
}
