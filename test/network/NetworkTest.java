package network;

import exceptions.DuplicateStopException;
import exceptions.TransportFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import routes.Route;
import stops.Stop;
import vehicles.Bus;
import vehicles.PublicTransport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class NetworkTest {
    @Rule
    public TestName name = new TestName();

    class TestingTransport extends PublicTransport {
        public TestingTransport(int id, int capacity, Route route) {
            super(id, capacity, route);
        }
    }

    class TestingRoute extends Route {
        public TestingRoute(String name, int routeNumber) {
            super(name, routeNumber);
        }

        @Override
        public String getType() {
            return "SolarX";
        }
    }

    private Network emptyNetwork;
    private Network alphaNetwork;
    private Network betaNetwork;

    private Route alphaRoute;

    private PublicTransport alphaVehicle;

    private Stop alphaStop;
    private Stop betaStop;

    private List<Route> alphaRoutes;

    private List<Stop> stops;
    private List<Stop> alphaStops;

    private List<PublicTransport> alphaVehicles;

    @Before
    public void setUp() throws IOException, TransportFormatException {

        emptyNetwork = new Network();
        alphaNetwork = new Network("networks/validFromSpec.txt");
        betaNetwork = new Network("networks/validEmptyRoute.txt");

        alphaRoute = new TestingRoute("alpha", 11);

        alphaVehicle = new TestingTransport(123, 50, alphaRoute);

        alphaStop = new Stop("UQ Lakes", 0, 0);
        betaStop = new Stop("City", 0, 1);

        alphaRoutes = alphaNetwork.getRoutes();

        stops = emptyNetwork.getStops();
        alphaStops = alphaNetwork.getStops();

        alphaVehicles = alphaNetwork.getVehicles();

    }

    @After
    public void tearDown() {
        System.out.println("Test method '" + name.getMethodName() +
                "' has been run");
    }

    @Test (expected = TransportFormatException.class)
    public void invalidBlankLines()
            throws IOException, TransportFormatException {
        new Network("networks/invalidBlankLines.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void invalidRouteEmptyStopNames()
            throws IOException, TransportFormatException {
        new Network("networks/invalidRouteEmptyStopNames.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void invalidRouteStops() throws IOException,
            TransportFormatException {
        new Network("networks/invalidRouteStops.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void invalidRouteTypes() throws IOException,
            TransportFormatException {
        new Network("networks/invalidRouteTypes.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void invalidStopCount() throws IOException,
            TransportFormatException {
        new Network("networks/invalidStopCount.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void invalidStopDelimiters() throws IOException,
            TransportFormatException {
        new Network("networks/invalidStopDelimiters.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void invalidTransportIntegers() throws IOException,
            TransportFormatException {
        new Network("networks/invalidTransportIntegers.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void invalidTransportPartsMissing() throws IOException,
            TransportFormatException {
        new Network("networks/invalidTransportPartsMissing.txt");
    }

    @Test
    public void validEmptyRoute() throws IOException, TransportFormatException {
        new Network("networks/validEmptyRoute.txt");
    }

    @Test
    public void validFromSpec() throws IOException, TransportFormatException {
        new Network("networks/validFromSpec.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void transportOnEmptyRoute()
            throws IOException, TransportFormatException {
        new Network("networks/transportOnEmptyRoute.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void extraNewlineCharacters()
            throws IOException, TransportFormatException {
        new Network("networks/extraNewlineCharacters.txt");
    }

    @Test
    public void routeCalledNull() throws IOException, TransportFormatException {
        new Network("networks/routeCalledNull.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void extraColonsInStopNames()
            throws IOException, TransportFormatException {
        new Network("networks/extraColonsInStopNames.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void emptyStopNames() throws IOException, TransportFormatException {
        new Network("networks/emptyStopNames.txt");
    }

    @Test
    public void multipleRoutesWithSameNumber()
            throws IOException, TransportFormatException {
        Network network = new
                Network("networks/multipleRoutesWithSameNumber.txt");
        assertEquals("red",
                network.getVehicles().get(2).getRoute().getName());
    }

    @Test
    public void linesWithExtraSpace()
            throws IOException, TransportFormatException {
        new Network("networks/linesWithExtraSpace.txt");
    }

    @Test
    public void emptyRouteName() throws IOException, TransportFormatException {
        new Network("networks/emptyRouteName.txt");
    }

    @Test
    public void minimumFile() throws IOException, TransportFormatException {
        new Network("networks/minimumFile.txt");
    }

    @Test
    public void hyphenRouteName() throws IOException, TransportFormatException {
        new Network("networks/hyphenRouteName.txt");
    }

    @Test (expected = IOException.class)
    public void networkNullFileName()
            throws IOException, TransportFormatException {
        new Network(null);
    }

    @Test (expected = TransportFormatException.class)
    public void extraSpaceRouteTypes()
            throws IOException, TransportFormatException {
        new Network("networks/extraSpaceRouteTypes.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void routeWithNonExistingStops()
            throws IOException, TransportFormatException {
        new Network("networks/routeWithNonExistingStops.txt");
    }

    @Test
    public void registrationNumberUntrimmed()
            throws IOException, TransportFormatException {
        Network network =
                new Network("networks/registrationNumberUntrimmed.txt");
        Bus bus = (Bus) network.getVehicles().get(2);
        String registrationNumber = bus.getRegistrationNumber();
        assertEquals("  ABC123", registrationNumber);
    }

    @Test
    public void stopNamesContainBars()
            throws IOException, TransportFormatException {
        new Network("networks/stopNamesContainBars.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void blankLinesInFile() throws IOException,
            TransportFormatException {
        new Network("networks/blankLinesInFile.txt");
    }

    @Test
    public void blankSpaceStopNames()
            throws IOException, TransportFormatException {
        new Network("networks/blankSpaceStopNames.txt");
    }

    @Test
    public void emptyCharacterStopNames()
            throws IOException, TransportFormatException {
        new Network("networks/emptyCharacterStopNames.txt");
    }

    @Test
    public void formatCoordinates() throws IOException,
            TransportFormatException {
        Network network = new Network("networks/formatCoordinates.txt");
        assertEquals(23, network.getStops().get(0).getX());
    }

//    @Test (expected = TransportFormatException.class)
//    public void routeNamesContainBars()
//            throws IOException, TransportFormatException {
//        new Network("networks/routeNamesContainBars.txt");
//    }

    @Test (expected = TransportFormatException.class)
    public void singleBlankLine() throws IOException, TransportFormatException {
        new Network("networks/singleBlankLine.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void routeWithEmptyStopNames() throws IOException,
            TransportFormatException {
        new Network("networks/routeWithEmptyStopNames.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void transportExtraDelimiters() throws IOException,
            TransportFormatException {
        new Network("networks/transportExtraDelimiters.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void stopExtraDelimiters() throws IOException,
            TransportFormatException {
        new Network("networks/stopExtraDelimiters.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void routeNameExtraComma() throws IOException,
            TransportFormatException {
        new Network("networks/routeExtraComma.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void routeExtraColon() throws IOException, TransportFormatException {
        new Network("networks/routeExtraColon.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void routeExtraBar() throws IOException, TransportFormatException {
        new Network("networks/routeExtraBar.txt");
    }

    @Test
    public void numberIntegerWithBlankSpace() throws IOException,
            TransportFormatException {
        new Network("networks/numberIntegerWithBlankSpace.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void incorrectFormatNumberInteger() throws IOException,
            TransportFormatException {
        new Network("networks/incorrectFormatNumberInteger.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void numberNotMatchItem() throws IOException,
            TransportFormatException {
        new Network("networks/numberNotMatchItem.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void negativeNumberInteger() throws IOException,
            TransportFormatException {
        new Network("networks/negativeNumberInteger.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void incorrectTransportType() throws IOException,
            TransportFormatException {
        new Network("networks/incorrectTransportType.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void transportIdNotInteger() throws IOException,
            TransportFormatException {
        new Network("networks/transportIdNotInteger.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void transportCapacityNotInteger() throws IOException,
            TransportFormatException {
        new Network("networks/transportCapacityNotInteger.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void transportRouteNumberNotInteger() throws IOException,
            TransportFormatException {
        new Network("networks/transportRouteNumberNotInteger.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void transportRouteNumberNotMatch() throws IOException,
            TransportFormatException {
        new Network("networks/transportRouteNumberNotMatch.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void transportReferenceWrongType() throws IOException,
            TransportFormatException {
        new Network("networks/transportReferenceWrongType.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void trainCarriageNotInteger() throws IOException,
            TransportFormatException {
        new Network("networks/trainCarriageNotInteger.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void transportMissingParts() throws IOException,
            TransportFormatException {
        new Network("networks/transportMissingParts.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void stopNoName() throws IOException, TransportFormatException {
        new Network("networks/stopNoName.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void stopNotIntegerX() throws IOException, TransportFormatException {
        new Network("networks/stopNotIntegerX.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void stopNotIntegerY() throws IOException, TransportFormatException {
        new Network("networks/stopNotIntegerY.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void stopMissingParts() throws IOException,
            TransportFormatException {
        new Network("networks/stopMissingParts.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void incorrectRouteType() throws IOException,
            TransportFormatException {
        new Network("networks/incorrectRouteType.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void routeNumberNotInteger() throws IOException,
            TransportFormatException {
        new Network("networks/routeNumberNotInteger.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void routeMissingParts() throws IOException,
            TransportFormatException {
        new Network("networks/routeMissingParts.txt");
    }

    @Test (expected = TransportFormatException.class)
    public void routeStringNoColon() throws IOException,
            TransportFormatException {
        new Network("networks/routeStringNoColon.txt");
    }

    @Test
    public void getRoutes() {
        assertTrue(emptyNetwork.getRoutes().isEmpty());
    }

    @Test
    public void shallowCopyRoutes() {
        alphaRoutes.add(null);
        assertFalse(alphaNetwork.getRoutes().contains(null));
    }

    @Test
    public void getStops() {
        assertTrue(emptyNetwork.getRoutes().isEmpty());
    }

    @Test
    public void shallowCopyStops() {
        alphaStops.add(null);
        assertFalse(alphaNetwork.getStops().contains(null));
    }

    @Test
    public void getVehicles() {
        assertTrue(emptyNetwork.getVehicles().isEmpty());
    }

    @Test
    public void shallowCopyVehicles() {
        alphaVehicles.add(null);
        assertFalse(alphaNetwork.getVehicles().contains(null));
    }

    @Test
    public void addNullRoute() {
        emptyNetwork.addRoute(null);
        assertTrue(emptyNetwork.getRoutes().isEmpty());
    }

    @Test
    public void addRoute() {
        emptyNetwork.addRoute(alphaRoute);
        assertTrue(emptyNetwork.getRoutes().contains(alphaRoute));
        assertEquals(1, emptyNetwork.getRoutes().size());
    }

    @Test
    public void addNullStop() throws DuplicateStopException {
        emptyNetwork.addStop(null);
        assertTrue(emptyNetwork.getStops().isEmpty());
    }

    @Test
    public void addStop() throws DuplicateStopException {
        emptyNetwork.addStop (alphaStop);
        assertTrue(emptyNetwork.getStops().contains(alphaStop));
        assertEquals(1, emptyNetwork.getStops().size());
    }

    @Test
    public void addNullStops() throws DuplicateStopException {
        stops.add(alphaStop);
        stops.add(null);
        emptyNetwork.addStops(stops);
        assertTrue(emptyNetwork.getStops().isEmpty());

    }

    @Test (expected = NullPointerException.class)
    public void addStopsNullObjects() throws DuplicateStopException {
        emptyNetwork.addStops(null);
        assertTrue(emptyNetwork.getStops().isEmpty());
    }

    @Test
    public void addStops() throws DuplicateStopException {
        stops.add(alphaStop);
        stops.add(betaStop);
        emptyNetwork.addStops(stops);
        assertTrue(emptyNetwork.getStops().contains(alphaStop));
        assertTrue(emptyNetwork.getStops().contains(betaStop));
        assertEquals(2, emptyNetwork.getStops().size());
    }

    @Test
    public void addNullVehicle() {
        emptyNetwork.addVehicle(null);
        assertTrue(emptyNetwork.getVehicles().isEmpty());
    }

    @Test
    public void addVehicle() {
        emptyNetwork.addVehicle(alphaVehicle);
        assertTrue(emptyNetwork.getVehicles().contains(alphaVehicle));
        assertEquals(1, emptyNetwork.getVehicles().size());
    }

    @Test
    public void saveTest() throws IOException {
        emptyNetwork.save("networks/emptyNetworkTest.txt");
        File emptyNetworkTest = new File(
                "networks/emptyNetworkTest.txt");
        BufferedReader emptyNetworkReader = new BufferedReader(
                new FileReader(emptyNetworkTest));
        assertEquals("0", emptyNetworkReader.readLine());
        assertEquals("0", emptyNetworkReader.readLine());
        assertEquals("0", emptyNetworkReader.readLine());
        emptyNetworkReader.close();

        alphaNetwork.save("networks/alphaNetworkTest.txt");
        File alphaNetworkTest = new File(
                "networks/alphaNetworkTest.txt");
        BufferedReader alphaNetworkReader = new BufferedReader(
                new FileReader(alphaNetworkTest));
        assertEquals("4", alphaNetworkReader.readLine());
        assertEquals("stop0:0:1", alphaNetworkReader.readLine());
        assertEquals("stop1:-1:0", alphaNetworkReader.readLine());
        assertEquals("stop2:4:2", alphaNetworkReader.readLine());
        assertEquals("stop3:2:-8", alphaNetworkReader.readLine());
        assertEquals("2", alphaNetworkReader.readLine());
        assertEquals("train,red,1:stop0|stop2|stop1",
                alphaNetworkReader.readLine());
        assertEquals("bus,blue,2:stop1|stop3|stop0",
                alphaNetworkReader.readLine());
        assertEquals("3", alphaNetworkReader.readLine());
        assertEquals("train,123,30,1,2",
                alphaNetworkReader.readLine());
        assertEquals("train,42,60,1,3",
                alphaNetworkReader.readLine());
        assertEquals("bus,412,20,2,ABC123",
                alphaNetworkReader.readLine());
        alphaNetworkReader.close();

        betaNetwork.save("networks/betaNetworkTest.txt");
        File betaNetworkTest = new File(
                "networks/betaNetworkTest.txt");
        BufferedReader betaNetworkReader = new BufferedReader(
                new FileReader(betaNetworkTest));
        assertEquals("4", betaNetworkReader.readLine());
        assertEquals("stop0:0:1", betaNetworkReader.readLine());
        assertEquals("stop1:-1:0", betaNetworkReader.readLine());
        assertEquals("stop2:4:2", betaNetworkReader.readLine());
        assertEquals("stop3:2:-8", betaNetworkReader.readLine());
        assertEquals("2", betaNetworkReader.readLine());
        assertEquals("train,red,1:stop0|stop2|stop1",
                betaNetworkReader.readLine());
        assertEquals("bus,blue,2:", betaNetworkReader.readLine());
        assertEquals("2", betaNetworkReader.readLine());
        assertEquals("train,123,30,1,2", betaNetworkReader.readLine());
        assertEquals("train,42,60,1,3", betaNetworkReader.readLine());
        betaNetworkReader.close();
    }

//    @Test
//    public void saveNull() throws IOException {
//        try {
//            emptyNetwork.save(null);
//        } catch (NullPointerException e) {
//            fail("NullPointerException: Null file name not handled.");
//        }
//        assertTrue(emptyNetwork.getRoutes().isEmpty());
//        assertTrue(emptyNetwork.getStops().isEmpty());
//        assertTrue(emptyNetwork.getVehicles().isEmpty());
//    }

    @Test
    public void saveEmptyNetwork() throws IOException,
            TransportFormatException {
        emptyNetwork.save("networks/emptyNetwork.txt");
        Network emptyNetworkTest =
                new Network("networks/emptyNetwork.txt");
        assertTrue(emptyNetworkTest.getStops().isEmpty());
        assertTrue(emptyNetworkTest.getRoutes().isEmpty());
        assertTrue(emptyNetworkTest.getVehicles().isEmpty());
    }

    @Test
    public void save() throws IOException, TransportFormatException {
        alphaNetwork.save("networks/alphaNetwork.txt");
        Network alphaNetworkTest =
                new Network("networks/alphaNetwork.txt");
        assertEquals(alphaNetwork.getStops(), alphaNetworkTest.getStops());
        assertEquals(alphaNetwork.getRoutes(), alphaNetworkTest.getRoutes());

        assertEquals(alphaNetwork.getVehicles().get(0).getType(),
                alphaNetworkTest.getVehicles().get(0).getType());
        assertEquals(alphaNetwork.getVehicles().get(0).getId(),
                alphaNetworkTest.getVehicles().get(0).getId());
        assertEquals(alphaNetwork.getVehicles().get(0).getCapacity(),
                alphaNetworkTest.getVehicles().get(0).getCapacity());
        assertEquals(alphaNetwork.getVehicles().get(0).getRoute(),
                alphaNetworkTest.getVehicles().get(0).getRoute());

        assertEquals(alphaNetwork.getVehicles().get(1).getType(),
                alphaNetworkTest.getVehicles().get(1).getType());
        assertEquals(alphaNetwork.getVehicles().get(1).getId(),
                alphaNetworkTest.getVehicles().get(1).getId());
        assertEquals(alphaNetwork.getVehicles().get(1).getCapacity(),
                alphaNetworkTest.getVehicles().get(1).getCapacity());
        assertEquals(alphaNetwork.getVehicles().get(1).getRoute(),
                alphaNetworkTest.getVehicles().get(1).getRoute());

        assertEquals(alphaNetwork.getVehicles().get(2).getType(),
                alphaNetworkTest.getVehicles().get(2).getType());
        assertEquals(alphaNetwork.getVehicles().get(2).getId(),
                alphaNetworkTest.getVehicles().get(2).getId());
        assertEquals(alphaNetwork.getVehicles().get(2).getCapacity(),
                alphaNetworkTest.getVehicles().get(2).getCapacity());
        assertEquals(alphaNetwork.getVehicles().get(2).getRoute(),
                alphaNetworkTest.getVehicles().get(2).getRoute());

        betaNetwork.save("networks/betaNetwork.txt");
        Network betaNetworkTest =
                new Network("networks/betaNetwork.txt");
        assertEquals(betaNetwork.getStops(), betaNetworkTest.getStops());
        assertEquals(betaNetwork.getRoutes(), betaNetworkTest.getRoutes());

        assertEquals(betaNetwork.getVehicles().get(0).getType(),
                betaNetworkTest.getVehicles().get(0).getType());
        assertEquals(betaNetwork.getVehicles().get(0).getId(),
                betaNetworkTest.getVehicles().get(0).getId());
        assertEquals(betaNetwork.getVehicles().get(0).getCapacity(),
                betaNetworkTest.getVehicles().get(0).getCapacity());
        assertEquals(betaNetwork.getVehicles().get(0).getRoute(),
                betaNetworkTest.getVehicles().get(0).getRoute());

        assertEquals(betaNetwork.getVehicles().get(1).getType(),
                betaNetworkTest.getVehicles().get(1).getType());
        assertEquals(betaNetwork.getVehicles().get(1).getId(),
                betaNetworkTest.getVehicles().get(1).getId());
        assertEquals(betaNetwork.getVehicles().get(1).getCapacity(),
                betaNetworkTest.getVehicles().get(1).getCapacity());
        assertEquals(betaNetwork.getVehicles().get(1).getRoute(),
                betaNetworkTest.getVehicles().get(1).getRoute());
    }
}