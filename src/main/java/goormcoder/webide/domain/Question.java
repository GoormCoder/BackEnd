package goormcoder.webide.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import goormcoder.webide.constants.QuestionConstants;
import jakarta.persistence.*;
import java.util.ArrayList;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Table(name = "t_quest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", nullable = false)
    private Long id;

    @NotBlank(message = QuestionConstants.TITLE_IS_BLANK)
    @Column(name = "quest_title", nullable = false)
    private String title;

    @Column(name = "quest_level", nullable = false)
    private Integer level;

    @NotBlank(message = QuestionConstants.CONTENT_IS_BLANK)
    @Column(name = "quest_content", nullable = false)
    @Lob
    private String content;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<TestCase> testCases = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Solve> solves = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "t_question_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<QuestionTag> tags = new ArrayList<>();

    public Question(String title, int level, String content) {
        this.title = title;
        this.level = level;
        this.content = content;
    }

    public void update(String title, int level, String content) {
        this.title = title;
        this.level = level;
        this.content = content;
    }

}
