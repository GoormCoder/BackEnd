package goormcoder.webide.dto;

import goormcoder.webide.domain.TestCase;

public record TestCaseValueDto(
        String input,
        String expectedOutput
) {

    public static TestCaseValueDto of(TestCase testCase) {
        return new TestCaseValueDto(
                testCase.getInput(),
                testCase.getOutput()
        );
    }

}
