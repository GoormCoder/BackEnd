package goormcoder.webide.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_testcase")
@NoArgsConstructor
@Getter
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "testcase_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", nullable = false)
    @JsonBackReference
    private Question question;

    @Column(name = "testcase_input", nullable = false)
    private String input;

    @Column(name = "testcase_output", nullable = false)
    private String output;

    public TestCase(Question question, String input, String output) {
        this.question = question;
        this.input = input;
        this.output = output;
    }

}
