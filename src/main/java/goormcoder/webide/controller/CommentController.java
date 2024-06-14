package goormcoder.webide.controller;

import goormcoder.webide.dto.request.CommentCreateDto;
import goormcoder.webide.dto.request.CommentUpdateDto;
import goormcoder.webide.dto.response.CommentFindAllDto;
import goormcoder.webide.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long boardId, @Valid @RequestBody CommentCreateDto commentCreateDto) {
        commentService.createComment(boardId, commentCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 생성되었습니다.");
    }

    //댓글 조회
    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<List<CommentFindAllDto>> getComments(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(boardId));
    }

    //댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        commentService.updateComment(commentId, commentUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정이 완료되었습니다.");
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제가 완료되었습니다.");
    }
}
