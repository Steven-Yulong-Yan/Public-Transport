package passengers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import stops.Stop;

import static org.junit.Assert.*;

public class PassengerTest {
    @Rule
    public TestName name = new TestName();

    private Stop alphaStop;
    private Stop betaStop;
    // Alpha -> Constructed without destination
    // Beta -> Constructed with destination
    private Passenger emptyNameAlpha;
    private Passenger emptyNameBeta;
    private Passenger nullNameAlpha;
    private Passenger nullNameBeta;
    private Passenger extraCharactersNameAlpha;
    private Passenger extraCharactersNameBeta;
    // Alex does not have a destination
    // Bran has a destination
    private Passenger alex;
    private Passenger bran;

    @Before
    public void setUp() {
        alphaStop = new Stop("Alpha",0, 0);
        betaStop = new Stop("Beta", 0, 1);
        emptyNameAlpha = new Passenger("");
        emptyNameBeta = new Passenger("", alphaStop);
        nullNameAlpha = new Passenger(null);
        nullNameBeta = new Passenger(null, alphaStop);
        extraCharactersNameAlpha =
                new Passenger("Contains\rExtra\nCharacters");
        extraCharactersNameBeta =
                new Passenger("Contains\rExtra\nCharacters", alphaStop);
        alex = new Passenger("Alex");
        bran = new Passenger("Bran", alphaStop);
    }

    @After
    public void tearDown() {
        System.out.println("Test method '" + name.getMethodName() +
                "' has been run");
    }

    @Test
    public void nullName() {
        assertEquals("If the given name is null, " +
                        "an empty string should be stored instead.",
                "", nullNameAlpha.getName());
        assertEquals("If the given name is null, " +
                        "an empty string should be stored instead.",
                "", nullNameBeta.getName());

    }

    @Test
    public void trimName() {
        assertEquals("If the given name contains any " +
                        "newline characters ('\\n') or carriage ('\\r'), " +
                        "they should be removed from the string before " +
                        "it is stored.",
                "ContainsExtraCharacters",
                extraCharactersNameAlpha.getName());
        assertEquals("If the given name contains any " +
                        "newline characters ('\\n') or carriage ('\\r'), " +
                        "they should be removed from the string before " +
                        "it is stored.",
                "ContainsExtraCharacters",
                extraCharactersNameBeta.getName());

    }

    @Test
    public void getName() {
        assertEquals("Alex", alex.getName());
        assertEquals("Bran", bran.getName());
    }

    @Test
    public void getDestination() {
        assertNull(alex.getDestination());
        assertEquals(alphaStop, bran.getDestination());
    }

    @Test
    public void setDestination() {
        alex.setDestination(null);
        bran.setDestination(null);
        assertNull(alex.getDestination());
        assertNull(bran.getDestination());
        alex.setDestination(betaStop);
        bran.setDestination(betaStop);
        assertEquals(betaStop, alex.getDestination());
        assertEquals(betaStop, bran.getDestination());
    }

    @Test
    public void toStringAnonymous() {
        assertEquals("Anonymous passenger", emptyNameAlpha.toString());
        assertEquals("Anonymous passenger", emptyNameBeta.toString());
    }

    @Test
    public void toStringValidName() {
        assertEquals("Passenger named Alex", alex.toString());
        assertEquals("Passenger named Bran", bran.toString());
    }

}