package goormcoder.webide.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "t_quest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", nullable = false)
    private Long id;

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Column(name = "quest_title", nullable = false)
    private String title;

    @Column(name = "quest_level", nullable = false)
    private Integer level;

    @NotBlank(message = "본문은 공백일 수 없습니다.")
    @Column(name = "quest_content", nullable = false)
    @Lob
    private String content;

    public Question(String title, int level, String content) {
        this.title = title;
        this.level = level;
        this.content = content;
    }

    public String getFormattedTitle() {
        return String.format("#%d [Lv.%d] %s", id, level, title);
    }

    public void update(String title, int level, String content) {
        this.title = title;
        this.level = level;
        this.content = content;
    }

}
