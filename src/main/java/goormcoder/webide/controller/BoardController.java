package goormcoder.webide.controller;

import goormcoder.webide.dto.request.BoardCreateDto;
import goormcoder.webide.dto.response.BoardFindAllDto;
import goormcoder.webide.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시글 등록
    @PostMapping("/board")
    public ResponseEntity<?> createBoard(@RequestBody BoardCreateDto boardCreateDto) {
        boardService.createBoard(boardCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 생성되었습니다.");
    }

    //게시글 조회
    @GetMapping("/board/all")
    public ResponseEntity<List<BoardFindAllDto>> getAllBoards() {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getAllBoards());
    }
}
