package goormcoder.webide.service;

import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.TestCase;
import goormcoder.webide.repository.TestCaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;

    @Transactional
    public TestCase create(Question question, String input, String output) {
        TestCase testCase = new TestCase(question, input, output);
        testCaseRepository.save(testCase);
        return testCase;
    }

    @Transactional
    public TestCase update(TestCase testCase, String input, String output) {
        testCase.update(input, output);
        testCaseRepository.save(testCase);
        return testCase;
    }

}
