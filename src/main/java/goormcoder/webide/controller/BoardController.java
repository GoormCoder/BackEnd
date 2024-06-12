package goormcoder.webide.controller;

import goormcoder.webide.dto.request.BoardCreateDto;
import goormcoder.webide.dto.request.BoardUpdateDto;
import goormcoder.webide.dto.response.BoardFindAllDto;
import goormcoder.webide.dto.response.BoardFindDto;
import goormcoder.webide.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시글 등록
    @PostMapping("/board")
    public ResponseEntity<?> createBoard(@Valid @RequestBody BoardCreateDto boardCreateDto) {
        boardService.createBoard(boardCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 생성되었습니다.");
    }

    //게시글 조회
    @GetMapping("/board/all")
    public ResponseEntity<List<BoardFindAllDto>> getAllBoards() {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getAllBoards());
    }

    //게시글 열람
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BoardFindDto> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getBoard(boardId));
    }

    //게시글 수정
    @PatchMapping("/board/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @Valid @RequestBody BoardUpdateDto boardUpdateDto) {
        boardService.updateBoard(boardId, boardUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 수정이 완료되었습니다.");
    }
}
