package goormcoder.webide.controller;

import goormcoder.webide.domain.enumeration.BoardType;
import goormcoder.webide.dto.request.BoardCreateDto;
import goormcoder.webide.dto.request.BoardUpdateDto;
import goormcoder.webide.dto.response.BoardFindAllDto;
import goormcoder.webide.dto.response.BoardFindDto;
import goormcoder.webide.dto.response.BoardSummaryDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Board", description = "게시글 관련 API")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PrincipalHandler principalHandler;

    //게시글 등록
    @PostMapping("/boards")
    @Operation(summary = "게시글 등록", description = "게시글을 등록합니다.")
    public ResponseEntity<?> createBoard(@Valid @RequestBody BoardCreateDto boardCreateDto) {
        boardService.createBoard(principalHandler.getMemberLoginId(), boardCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 생성되었습니다.");
    }

    //게시글 조회
    @GetMapping("/boards/all")
    @Operation(summary = "게시글 전체 조회", description = "전체 게시글을 조회합니다.")
    public ResponseEntity<Page<BoardSummaryDto>> getAllBoards(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getAllBoards(pageable));
    }

    //게시글 유형별 조회
    @GetMapping("/boards")
    @Operation(summary = "게시글 유형별 조회", description = "게시글 유형(FREE_BOARD, QUESTION_BOARD)에 따라 조회합니다.")
    public ResponseEntity<Page<BoardSummaryDto>> getBoardsByType(@RequestParam BoardType boardType, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getBoardsByType(boardType, pageable));
    }

    //게시글 열람
    @GetMapping("/boards/{boardId}")
    @Operation(summary = "게시글 열람", description = "특정 게시글을 열람합니다.")
    public ResponseEntity<BoardFindDto> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getBoard(boardId));
    }

    //게시글 수정
    @PatchMapping("/boards/{boardId}")
    @Operation(summary = "게시글 수정", description = "특정 게시글을 수정합니다.")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @Valid @RequestBody BoardUpdateDto boardUpdateDto) {
        boardService.updateBoard(principalHandler.getMemberLoginId(), boardId, boardUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 수정이 완료되었습니다.");
    }

    //게시글 삭제
    @DeleteMapping("/boards/{boardId}")
    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(principalHandler.getMemberLoginId(), boardId);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 삭제가 완료되었습니다.");
    }
}
