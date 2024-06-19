package goormcoder.webide.util.javaCompiler;

import goormcoder.webide.domain.enumeration.SolveResult;
import goormcoder.webide.dto.TestCaseValueDto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public final class ScoringUtil {

    public static SolveResult getSolveResult(String code, List<TestCaseValueDto> testCases) {

        // Java 컴파일러 생성
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // 메모리 내 컴파일을 위한 파일 관리자
        MemoryFileManager fileManager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null));

        // 소스 파일 생성
        JavaFileObject javaFile = new MemoryJavaFileObject("Main", code);

        // 컴파일
        compiler.getTask(null, fileManager, null, null, null, Arrays.asList(javaFile)).call();

        // 클래스 로드
        MemoryClassLoader classLoader = new MemoryClassLoader(fileManager.getClassBytes());

        // 메인 메소드 로드
        Method method;
        try {
            method = classLoader.loadClass("Main").getMethod("main", String[].class);
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            return SolveResult.COMPILE_ERROR;
        }

        for (TestCaseValueDto testCase : testCases) {
            String input = testCase.input();
            String expectedOutput = testCase.expectedOutput();

            // 입력 설정
            InputStream originalIn = System.in;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            // 결과를 캡처하기 위한 준비
            PrintStream originalOut = System.out;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(bos));

            // 메소드 실행
            try {
                method.invoke(null, (Object) new String[0]);
            }
            catch (InvocationTargetException | IllegalAccessException e) {
                return SolveResult.RUNTIME_ERROR;
            }

            // 입력 및 출력 복원
            System.setIn(originalIn);
            System.setOut(originalOut);

            // 출력된 결과 검증
            String actualOutput = bos.toString().trim();
            if (!actualOutput.equals(expectedOutput)) {
                return SolveResult.WRONG;
            }
        }
        return SolveResult.CORRECT;
    }

    public static void main(String[] args) {
        try {
            String sourceCode = "public class Main { public static void main(String[] args) { "
                    + "System.out.println(\"Hello World\"); } }";

            getSolveResult(
                    sourceCode,
                    List.of(
                            new TestCaseValueDto("Hello World\n", "Hello World"),
                            new TestCaseValueDto("0\n", "Hello World")
                    )
            );
        } catch (Exception e) {
            System.out.println("_---------------------------");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
