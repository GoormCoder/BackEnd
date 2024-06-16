package goormcoder.webide.controller;

import goormcoder.webide.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    //좋아요 생성
    @PostMapping("/boards/{boardId}/like")
    @Operation(summary = "게시글 좋아요 생성", description = "특정 게시글 좋아요를 누릅니다.")
    public ResponseEntity<?> createLike(@RequestParam Long memberId, @PathVariable Long boardId) {
        likeService.createLike(memberId, boardId);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요가 완료되었습니다.");
    }

    //좋아요 삭제
    @PostMapping("/boards/{boardId}/unlike")
    @Operation(summary = "게시글 좋아요 취소", description = "특정 게시글 좋아요를 취소합니다.")
    public ResponseEntity<?> unLike(@RequestParam Long memberId, @PathVariable Long boardId) {
        likeService.deleteLike(memberId, boardId);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요가 취소되었습니다.");
    }
}
