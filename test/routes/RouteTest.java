package routes;

import exceptions.TransportFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import stops.Stop;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RouteTest {
    @Rule
    public TestName name = new TestName();

    private Route alphaRoute;
    private Route betaRoute;
    private Route gammaRoute;

    private Stop alphaStop;
    private Stop betaStop;
    private Stop gammaStop;
    private Stop deltaStop;
    private Stop epsilonStop;
    private Stop zetaStop;

    private String alphaString;

    private String betaString;
    private String gammaString;
    private String deltaString;
    private String epsilonString;
    private String zetaString;
    private String etaString;
    private String thetaString;
    private String iotaString;
    private String kappaString;
    private String lambdaString;
    private String muString;

    private List<Stop> initStops;

    private List<Stop> alphaStops;
    private List<Stop> betaStops;
    private List<Stop> gammaStops;

    @Before
    public void setUp() {

        alphaRoute = new BusRoute("red", 1);
        betaRoute = new BusRoute("", 1);
        gammaRoute = new BusRoute(" ", 1);

        alphaStop = new Stop("UQ Lakes", 0, 0);
        betaStop = new Stop("City", 0, 1);
        gammaStop = new Stop("Valley", 1, 0);
        deltaStop = new Stop("South Bank", 1, 1);
        epsilonStop = new Stop("Hamilton", 1, 2);
        zetaStop = new Stop("UQ Lakes", 2, 2);

        alphaRoute.addStop(alphaStop);
        alphaRoute.addStop(betaStop);
        alphaRoute.addStop(gammaStop);

        alphaString = "bus,red,1:UQ Lakes|City|Valley";

        betaString = "plane,red,1:UQ Lakes|City|Valley";
        gammaString = "bus,red,a:UQ Lakes|City|Valley";
        deltaString = "bus,red,1,:UQ Lakes|City|Valley";
        epsilonString = "bus,red,1:UQ Lakes:|City|Valley";
        zetaString = "bus,red,1:UQ Lakes|City|Valley|";
        etaString = "bus,red:UQ Lakes|City|Valley";
        thetaString = "bus,red,1:South Bank|Hamilton";
        iotaString = "bus,red,1:UQ Lakes|City|Valley|UQ Lakes";
        kappaString = "bus,,1:UQ Lakes|City|Valley";
        lambdaString = "bus, ,1:UQ Lakes|City|Valley";
        muString = "";

        initStops = new ArrayList<>();
        alphaStops = new ArrayList<>();
        betaStops = new ArrayList<>();
        gammaStops = new ArrayList<>();

        alphaStops.add(alphaStop);
        alphaStops.add(betaStop);
        alphaStops.add(gammaStop);

        betaStops.add(alphaStop);
        betaStops.add(betaStop);
        betaStops.add(gammaStop);
        betaStops.add(deltaStop);
        betaStops.add(epsilonStop);

        gammaStops.add(alphaStop);
        gammaStops.add(betaStop);
        gammaStops.add(gammaStop);
        gammaStops.add(zetaStop);

    }

    @After
    public void tearDown() {
        System.out.println("Test method '" + name.getMethodName() +
                "' has been run");
    }

    @Test
    public void encode() {
        assertEquals("bus,red,1:UQ Lakes|City|Valley",
                alphaRoute.encode());
    }

    @Test
    public void decode() throws TransportFormatException {
        Route alphaDecoded = Route.decode(alphaString, alphaStops);
        assertEquals(alphaRoute, alphaDecoded);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIncorrectRouteType() throws TransportFormatException {
        Route.decode(betaString, alphaStops);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeRouteNumberNotInteger() throws TransportFormatException {
        Route.decode(gammaString, alphaStops);
    }

//    @Test (expected = TransportFormatException.class)
//    public void decodeExtraCommas() throws TransportFormatException {
//        Route.decode(deltaString, alphaStops);
//    }

//    @Test (expected = TransportFormatException.class)
//    public void decodeExtraColons() throws TransportFormatException {
//        Route.decode(epsilonString, alphaStops);
//    }

    @Test (expected = TransportFormatException.class)
    public void decodeExtraBars() throws TransportFormatException {
        Route.decode(zetaString, alphaStops);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeMissingParts() throws TransportFormatException {
        Route.decode(muString, initStops);
        Route.decode(etaString, alphaStops);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeStopNameNotMatch() throws TransportFormatException {
        Route.decode(thetaString, alphaStops);
    }

    @Test
    public void decodeAddStop() throws TransportFormatException {
        Route alphaDecoded = Route.decode(alphaString, alphaStops);
        assertEquals(alphaStops, alphaDecoded.getStopsOnRoute());
    }

    @Test
    public void decodeAddOnlyCorrectStops() throws TransportFormatException {
        Route iotaDecoded = Route.decode(alphaString, betaStops);
        assertEquals(alphaStops, iotaDecoded.getStopsOnRoute());
    }

    @Test
    public void decodeFirstOccurrenceIfDuplicates() throws TransportFormatException {
        initStops.add(alphaStop);
        initStops.add(betaStop);
        initStops.add(gammaStop);
        initStops.add(alphaStop);
        Route iotaDecoded = Route.decode(iotaString, gammaStops);
        assertEquals(initStops, iotaDecoded.getStopsOnRoute());
    }

    @Test
    public void decodeEmptyRouteName() throws TransportFormatException {
        Route kappaDecoded = Route.decode(kappaString, alphaStops);
        Route lambdaDecoded = Route.decode(lambdaString, alphaStops);
        assertEquals(betaRoute, kappaDecoded);
        assertEquals(gammaRoute, lambdaDecoded);
    }

}