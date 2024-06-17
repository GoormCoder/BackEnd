package goormcoder.webide.service;

import goormcoder.webide.common.dto.ErrorMessage;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.TestCase;
import goormcoder.webide.repository.TestCaseRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public TestCase update(Long id, String input, String output) {
        TestCase testCase = this.findById(id);
        testCase.update(input, output);
        testCaseRepository.save(testCase);
        return testCase;
    }

    public TestCase findById(Long id) {
        return testCaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TESTCASE_NOT_FOUND.getMessage()));
    }

    @Transactional
    public void deleteById(Long id) {
        testCaseRepository.deleteById(id);
    }

}
