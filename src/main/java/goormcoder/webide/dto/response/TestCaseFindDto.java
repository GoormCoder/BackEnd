package goormcoder.webide.dto.response;

import goormcoder.webide.domain.TestCase;

public record TestCaseFindDto (Long id, String input, String output) {

    public static TestCaseFindDto of(TestCase testCase) {
        return new TestCaseFindDto(
                testCase.getId(),
                testCase.getInput(),
                testCase.getOutput()
        );
    }

}
