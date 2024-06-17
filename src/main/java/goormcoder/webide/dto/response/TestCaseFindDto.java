package goormcoder.webide.dto.response;

import goormcoder.webide.domain.TestCase;

public record TestCaseFindDto (String input, String output) {

    public static TestCaseFindDto of(TestCase testCase) {
        return new TestCaseFindDto(
                testCase.getInput(),
                testCase.getOutput()
        );
    }

}
