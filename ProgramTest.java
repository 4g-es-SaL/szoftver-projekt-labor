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
    public void Case1() throws Exception {
        runTestCase(1);
    }

    @Test
    public void Case2() throws Exception {
        runTestCase(2);
    }

    @Test
    public void Case3() throws Exception {
        runTestCase(3);
    }

    @Test
    public void Case4() throws Exception {
        runTestCase(4);
    }

    @Test
    public void Case5() throws Exception {
        runTestCase(5);
    }

    @Test
    public void Case6() throws Exception {
        runTestCase(6);
    }

    @Test
    public void Case7() throws Exception {
        runTestCase(7);
    }

    @Test
    public void Case8() throws Exception {
        runTestCase(8);
    }

    @Test
    public void Case9() throws Exception {
        runTestCase(9);
    }

    @Test
    public void Case10() throws Exception {
        runTestCase(10);
    }

    @Test
    public void Case11() throws Exception {
        runTestCase(11);
    }

    @Test
    public void Case12() throws Exception {
        runTestCase(12);
    }

    @Test
    public void Case13() throws Exception {
        runTestCase(13);
    }

    @Test
    public void Case14() throws Exception {
        runTestCase(14);
    }

    /**
     * @param i The id number of the test case
     * Runs the given test case
     */
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
        String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFileName)));
        assertEquals(expectedOutput.replace("\r",""), output.replace("\r",""));
    }

}