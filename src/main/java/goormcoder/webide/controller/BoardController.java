package goormcoder.webide.controller;

import goormcoder.webide.dto.request.BoardCreateDto;
import goormcoder.webide.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<?> createBoard(@RequestBody BoardCreateDto boardCreateDto) {
        boardService.createBoard(boardCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 생성되었습니다.");
    }
}
