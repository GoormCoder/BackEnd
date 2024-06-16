package goormcoder.webide.domain;

import goormcoder.webide.dto.request.CommentCreateDto;
import goormcoder.webide.dto.request.CommentUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@Table(name = "t_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Comment of(final String content, final Board board, final Member member) {
        return Comment.builder()
                .content(content)
                .board(board)
                .member(member)
                .build();
    }

    @Builder
    public Comment(String content, Board board, Member member) {
        this.content = content;
        this.board = board;
        this.member = member;
    }

    public void patch(String content) {
        if (isModified(content)) {
            this.content = content;
        }
    }

    private boolean isModified(String content) {
        return !Objects.equals(this.content, content);
    }
}
