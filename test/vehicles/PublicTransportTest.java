package vehicles;

import exceptions.TransportFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import routes.BusRoute;
import routes.FerryRoute;
import routes.Route;
import routes.TrainRoute;
import stops.Stop;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PublicTransportTest {
    @Rule
    public TestName name = new TestName();

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
    private String nuString;
    private String xiString;
    private String omicronString;

    private Route busRoute;
    private Route ferryAlphaRoute;
    private Route ferryBetaRoute;
    private Route trainAlphaRoute;
    private Route trainBetaRoute;

    private List<PublicTransport> initTransports;

    private Stop alphaStop;

    private PublicTransport alphaBus;
    private PublicTransport alphaFerry;
    private PublicTransport alphaTrain;

    private List<Route> alphaRoutes;
    private List<Route> betaRoutes;
    private List<Route> gammaRoutes;

    @Before
    public void setUp() {

        alphaString = "train,3,100,3,5";
        betaString = "train,3,10,0,3,5";
        gammaString = "plane,3,100,3,5";
        deltaString = "train,a,100,3,5";
        epsilonString = "train,3,a,3,5";
        zetaString = "train,3,100,a,5";
        etaString = "train,3,100,3";
        thetaString = "train,3,100,5,5";
        iotaString = "train,3,100,2,5";
        kappaString = "train,3,100,3,a";
        lambdaString = "bus,1,30,1,ABC123";
        muString = "bus,1,30,2,ABC123";
        nuString = "ferry,2,50,2,CityCat";
        xiString = "";
        omicronString = "train, 3 , 100 , 3 , 5 ";

        busRoute = new BusRoute("Bus Route", 1);
        ferryAlphaRoute = new FerryRoute("Ferry Route 1", 2);
        ferryBetaRoute = new FerryRoute("Ferry Route 2", 2);
        trainAlphaRoute = new TrainRoute("Train Route", 3);
        trainBetaRoute = new TrainRoute("Train Route", 4);

        initTransports = busRoute.getTransports();

        alphaStop = new Stop("Alpha", 0, 0);

        alphaBus = new Bus(1, 30, busRoute, "ABC123");
        alphaFerry = new Ferry(2, 50, ferryAlphaRoute, "CityCat");
        alphaTrain = new Train(3, 100, trainAlphaRoute, 5);

        alphaRoutes = new ArrayList<>();
        betaRoutes = new ArrayList<>();
        gammaRoutes = new ArrayList<>();

        trainAlphaRoute.addStop(alphaStop);
        trainBetaRoute.addStop(alphaStop);
        ferryAlphaRoute.addStop(alphaStop);
        ferryBetaRoute.addStop(alphaStop);

        alphaRoutes.add(trainAlphaRoute);
        alphaRoutes.add(trainBetaRoute);
        betaRoutes.add(busRoute);
        gammaRoutes.add(ferryAlphaRoute);
        gammaRoutes.add(ferryBetaRoute);
    }

    @After
    public void tearDown() {
        System.out.println("Test method '" + name.getMethodName() +
                "' has been run");
    }

    @Test
    public void encode() {
        assertEquals("bus,1,30,1,ABC123", alphaBus.encode());
        assertEquals("ferry,2,50,2,CityCat", alphaFerry.encode());
        assertEquals("train,3,100,3,5", alphaTrain.encode());
    }

    @Test
    public void decode() throws TransportFormatException {
        assertEquals(3, PublicTransport
        .decode(alphaString, alphaRoutes).getId());
        assertEquals(100, PublicTransport
        .decode(alphaString, alphaRoutes).getCapacity());
        assertEquals(trainAlphaRoute, PublicTransport
        .decode(alphaString, alphaRoutes).getRoute());
    }

    @Test
    public void decodeIntegerTrim() throws TransportFormatException {
        assertEquals(3, PublicTransport
                .decode(omicronString, alphaRoutes).getId());
        assertEquals(100, PublicTransport
                .decode(omicronString, alphaRoutes).getCapacity());
        assertEquals(trainAlphaRoute, PublicTransport
                .decode(omicronString, alphaRoutes).getRoute());
    }

    @Test (expected = TransportFormatException.class)
    public void decodeTransportStringNull() throws TransportFormatException {
        PublicTransport.decode(null, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeExistingRoutesNull() throws TransportFormatException {
        PublicTransport.decode(alphaString, null);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeExtraDelimiters() throws TransportFormatException {
        PublicTransport.decode(betaString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIncorrectTransportType() throws TransportFormatException {
        PublicTransport.decode(gammaString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIntegerValueId() throws TransportFormatException {
        PublicTransport.decode(deltaString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIntegerValueCapacity() throws TransportFormatException {
        PublicTransport.decode(epsilonString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIntegerValueRouteNumber() throws TransportFormatException {
        PublicTransport.decode(zetaString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeMissingParts() throws TransportFormatException {
        PublicTransport.decode(xiString, alphaRoutes);
        PublicTransport.decode(etaString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeRouteNumberMatch() throws TransportFormatException {
        PublicTransport.decode(thetaString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeRouteTypeMatch() throws TransportFormatException {
        PublicTransport.decode(iotaString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIntegerValueCarriage() throws TransportFormatException {
        PublicTransport.decode(kappaString, alphaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeEmptyRoute() throws TransportFormatException {
        PublicTransport.decode(lambdaString, betaRoutes);
    }

    @Test (expected = TransportFormatException.class)
    public void decodeIncompatibleType() throws TransportFormatException {
        busRoute.addStop(alphaStop);
        PublicTransport.decode(muString, betaRoutes);
    }

    @Test
    public void decodeUseFirstOccurrenceIfDuplicates() throws TransportFormatException {
        assertEquals(ferryAlphaRoute, PublicTransport
        .decode(nuString, gammaRoutes).getRoute());
    }

    @Test
    public void decodeAddTransport() throws TransportFormatException {
        busRoute.addStop(alphaStop);
        alphaBus = PublicTransport.decode(lambdaString, betaRoutes);
        initTransports.add(alphaBus);
        assertEquals(initTransports, busRoute.getTransports());
    }

}