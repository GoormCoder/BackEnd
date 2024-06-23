package goormcoder.webide.service;

import goormcoder.webide.constants.ErrorMessages;
import goormcoder.webide.domain.Question;
import goormcoder.webide.domain.TestCase;
import goormcoder.webide.dto.response.TestCaseFindDto;
import goormcoder.webide.repository.TestCaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.TESTCASE_NOT_FOUND.getMessage()));
    }

    @Transactional
    public void deleteById(Long id) {
        testCaseRepository.deleteById(id);
    }

    public Page<TestCaseFindDto> findAllByQuestionId(Long id, Pageable pageable) {
        return testCaseRepository
                .findAllByQuestionId(
                        id,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort())
                )
                .map(TestCaseFindDto::of);
    }

}
