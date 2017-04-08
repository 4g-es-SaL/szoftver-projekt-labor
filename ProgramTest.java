import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.NoSuchElementException;

/**
 * Created by matech on 2017. 04. 08..
 */
class ProgramTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeClass
    private void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public boolean runTestCases() throws FileNotFoundException {
        for (int i = 1; i < 2; i++) {
            runTestCase(i);
        }

        return true;
    }

    public boolean runTestCase(int i) throws FileNotFoundException {
        String fileName = String.valueOf(i) + ".txt";
        System.setIn(new FileInputStream(fileName));
        try {
            Program.main(new String[0]);
        } catch(NoSuchElementException e) {
            //Our file has no more lines and scanner is upset.
        }
        String output = outContent.toString();
        return true;
    }

    @AfterClass
    private void tearDown() {
        System.setOut(null);
        System.setErr(null);
    }

}