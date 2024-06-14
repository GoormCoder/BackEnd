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
public class Question {

    private static final int MIN_LEVEL = 0;
    private static final int MAX_LEVEL = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "quest_title", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "quest_content", nullable = false)
    @Lob
    private String content;

    @Min(value = MIN_LEVEL)
    @Max(value = MAX_LEVEL)
    @Column(name = "quest_level", nullable = false)
    private Integer level;

    public Question(String title, String content, int level) {
        this.title = title;
        this.content = content;
        this.level = level;
    }

    public String getFormattedTitle() {
        return String.format("#%d [Lv.%d] %s", id, level, title);
    }

}
