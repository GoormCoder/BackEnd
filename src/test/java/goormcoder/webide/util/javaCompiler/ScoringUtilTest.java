package goormcoder.webide.util.javaCompiler;

import goormcoder.webide.domain.enumeration.SolveResult;
import goormcoder.webide.dto.TestCaseValueDto;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScoringUtilTest {

    @Test
    @DisplayName("올바른 출력값을 출력하면 정답 처리를 받는다.")
    void getSolveResultCorrect() {
        String code = "public class Main {\n"
                + "    public static void main(String[] args) {\n"
                + "        System.out.println(\"hello\");\n"
                + "    }\n"
                + "}";
        List<TestCaseValueDto> testCaseValues = List.of(new TestCaseValueDto("", "hello"));

        SolveResult result = ScoringUtil.getSolveResult(code, testCaseValues);

        Assertions.assertEquals(result, SolveResult.CORRECT);
    }

    @Test
    @DisplayName("잘못된 출력값을 출력하면 오답 처리를 받는다.")
    void getSolveResultWrong() {
        String code = "public class Main {\n"
                + "    public static void main(String[] args) {\n"
                + "        System.out.println(\"hello\");\n"
                + "    }\n"
                + "}";
        List<TestCaseValueDto> testCaseValues = List.of(new TestCaseValueDto("", "hi"));

        SolveResult result = ScoringUtil.getSolveResult(code, testCaseValues);

        Assertions.assertEquals(result, SolveResult.WRONG);
    }

    @Test
    @DisplayName("입력값과 출력값에 올바르게 대응한다.")
    void getSolveResultWithInput() {
        String code = "import java.io.*;\n"
                + "public class Main {\n"
                + "    public static void main(String[] args) throws IOException {\n"
                + "        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));\n"
                + "        int input = Integer.parseInt(br.readLine());\n"
                + "        System.out.println(input * 2);\n"
                + "    }\n"
                + "}";
        List<TestCaseValueDto> testCaseValues = List.of(
                new TestCaseValueDto("0", "0"),
                new TestCaseValueDto("-1", "-2"),
                new TestCaseValueDto("1", "2"),
                new TestCaseValueDto("-1000000000", "-2000000000"),
                new TestCaseValueDto("1000000000", "2000000000")
        );

        SolveResult result = ScoringUtil.getSolveResult(code, testCaseValues);

        Assertions.assertEquals(result, SolveResult.CORRECT);
    }

    @Test
    @DisplayName("클래스명이 잘못 작성된 코드는 컴파일 에러 처리를 받는다.")
    void getSolveResultCompileError() {
        String code = "public class WRONG {\n"
                + "    public static void main(String[] args) {\n"
                + "        System.out.println(\"hi\");\n"
                + "    }\n"
                + "}";
        List<TestCaseValueDto> testCaseValues = List.of(new TestCaseValueDto("", ""));

        SolveResult result = ScoringUtil.getSolveResult(code, testCaseValues);

        Assertions.assertEquals(result, SolveResult.COMPILE_ERROR);
    }

    @Test
    @DisplayName("인덱스의 범위를 벗어나는 코드는 런타임 에러 처리를 받는다.")
    void getSolveResultRuntimeError() {
        String code = "public class Main {\n"
                + "    public static void main(String[] args) {\n"
                + "        int[] arr = new int[1];"
                + "        System.out.println(arr[-1]);\n"
                + "    }\n"
                + "}";
        List<TestCaseValueDto> testCaseValues = List.of(new TestCaseValueDto("", ""));

        SolveResult result = ScoringUtil.getSolveResult(code, testCaseValues);

        Assertions.assertEquals(result, SolveResult.RUNTIME_ERROR);
    }

}
