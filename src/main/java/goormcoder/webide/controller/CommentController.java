package goormcoder.webide.controller;

import goormcoder.webide.dto.request.CommentCreateDto;
import goormcoder.webide.dto.request.CommentUpdateDto;
import goormcoder.webide.dto.response.CommentFindAllDto;
import goormcoder.webide.jwt.PrincipalHandler;
import goormcoder.webide.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Comment", description = "댓글 관련 API")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PrincipalHandler principalHandler;

    //댓글 생성
    @PostMapping("/boards/{boardId}/comments")
    @Operation(summary = "댓글 생성", description = "댓글을 생성합니다.")
    public ResponseEntity<?> createComment(@PathVariable Long boardId, @Valid @RequestBody CommentCreateDto commentCreateDto) {
        commentService.createComment(boardId, principalHandler.getMemberLoginId(), commentCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 생성되었습니다.");
    }

    //댓글 조회
    @GetMapping("/boards/{boardId}/comments")
    @Operation(summary = "댓글 조회", description = "전체 댓글을 조회합니다.")
    public ResponseEntity<List<CommentFindAllDto>> getComments(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(boardId));
    }

    //댓글 수정
    @PatchMapping("/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "특정 댓글을 수정합니다.")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        commentService.updateComment(principalHandler.getMemberLoginId(), commentId, commentUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정이 완료되었습니다.");
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(principalHandler.getMemberLoginId(), commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제가 완료되었습니다.");
    }
}
