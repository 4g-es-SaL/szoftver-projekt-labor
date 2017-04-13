import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;


/**
 * It is a JUnit class, that helps to compare the expected and the real output of the program.
 */
public class ProgramTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void runTestCases() throws IOException {
        final int TEST_Case = 12; //Change for the number of the test case or -1 to run all tests
        if (TEST_Case > 0) {
            runTestCase(TEST_Case);
        } else {
            for (int i = 1; i < 15; i++) {
                runTestCase(i);
            }
        }

    }

    public void runTestCase(int i) throws IOException {
        String InputFileName = "test/" + String.valueOf(i) + "input.txt";
        System.setIn(new FileInputStream(InputFileName));
        try {
            Program.main(new String[0]);
        } catch(NoSuchElementException e) {
            //Our file has no more lines and scanner is just upset.
        }
        String expectedOutputFileName = "test/" + String.valueOf(i) + "output.txt";
        String output = outContent.toString();
        new FileInputStream((expectedOutputFileName));
        String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFileName)));
        assertEquals(expectedOutput.replace("\r",""), output.replace("\r",""));
    }

}