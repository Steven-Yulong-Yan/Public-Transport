package stops;

import exceptions.TransportFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static org.junit.Assert.*;

public class StopFormatTest {
    @Rule
    public TestName name = new TestName();

    private Stop alphaStop;

    private String alphaString;
    private String betaString;
    private String gammaString;
    private String deltaString;
    private String epsilonString;

    @Before
    public void setUp() {

        alphaStop = new Stop("Alpha", 1, 2);

        alphaString = "Alpha:1:2";
        betaString = "Alp:ha:1:2";
        gammaString = "Alpha:1:";
        deltaString = "Alpha:a:2";
        epsilonString = "Alpha:1:a";

    }

    @After
    public void tearDown() {
        System.out.println("Test method '" + name.getMethodName() +
                "' has been run");
    }

    @Test
    public void encode() {
        assertEquals("Alpha:1:2", alphaStop.encode());
    }

    @Test
    public void decode() throws TransportFormatException {
        assertEquals("Alpha", Stop.decode(alphaString).getName());
        assertEquals(1, Stop.decode(alphaString).getX());
        assertEquals(2, Stop.decode(alphaString).getY());
    }

    @Test (expected = TransportFormatException.class)
    public void decodeNull() throws TransportFormatException {
        Stop.decode(null);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeExtraDelimiters() throws TransportFormatException {
        Stop.decode(betaString);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeMissingParts() throws TransportFormatException {
        Stop.decode(gammaString);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIntegerValueX() throws TransportFormatException {
        Stop.decode(deltaString);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIntegerValueY() throws TransportFormatException {
        Stop.decode(epsilonString);
    }

}