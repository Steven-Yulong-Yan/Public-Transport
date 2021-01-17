package stops;

import exceptions.NoNameException;
import exceptions.OverCapacityException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import passengers.Passenger;
import routes.BusRoute;
import routes.FerryRoute;
import routes.Route;
import routes.TrainRoute;
import vehicles.Bus;
import vehicles.Ferry;
import vehicles.PublicTransport;
import vehicles.Train;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class StopTest {
    @Rule
    public TestName name = new TestName();

    private Stop alphaStop;
    private Stop alphaCopy;
    private Stop betaStop;
    private Stop gammaStop;
    private Stop deltaStop;
    private Stop extraCharacterNameStop;

    private List<Passenger> initPassengers;

    private Passenger alec;
    private Passenger bran;
    private Passenger cato;

    private List<Route> initRoutes;

    private Route ferryRoute;
    private Route busRoute;
    private Route trainRoute;

    private List<PublicTransport> initVehicles;

    private Ferry ferry;
    private Bus bus;
    private Train train;

    private List<Stop> initNeighbours;

    @Before
    public void setUp() {

        alphaStop = new Stop("Alpha", 0, 0);
        alphaCopy = new Stop("Alpha", 0, 0);
        betaStop = new Stop("Beta", 0, 0);
        gammaStop = new Stop("Gamma", 0, 4);
        deltaStop = new Stop("Delta", 2, -4);
        extraCharacterNameStop =
                new Stop("Contains\rExtra\nCharacters", 0, 0);

        initPassengers = alphaStop.getWaitingPassengers();

        alec = new Passenger("Alec", alphaStop);
        bran = new Passenger("Bran", alphaStop);
        cato = new Passenger("Cato", alphaStop);

        initRoutes = alphaStop.getRoutes();

        busRoute = new BusRoute("Bus Route", 1);
        trainRoute = new TrainRoute("Train Route", 2);
        ferryRoute = new FerryRoute("Ferry Route", 3);

        initVehicles = alphaStop.getVehicles();

        bus = new Bus(11, 50, busRoute, "XX");
        train = new Train(12, 500, trainRoute, 10);
        ferry = new Ferry(13, 100, ferryRoute, "CityCat");

        initNeighbours = alphaStop.getNeighbours();
    }

    @After
    public void tearDown() {
        System.out.println("Test method '" + name.getMethodName() +
                "' has been run");
    }

    @Test(expected = NoNameException.class)
    public void nullNameTest() {
        Stop nullNameStop = new Stop(null, 0, 0);
    }

    @Test(expected = NoNameException.class)
    public void EmptyNameTest() {
        Stop emptyNameStop = new Stop("", 0, 0);
    }

    @Test
    public void trimName() {
        assertEquals("ContainsExtraCharacters",
                extraCharacterNameStop.getName());
    }

    @Test
    public void getName() {
        assertEquals("Alpha", alphaStop.getName());
    }

    @Test
    public void getX() {
        assertEquals(0, alphaStop.getX());
    }

    @Test
    public void getY() {
        assertEquals(0, alphaStop.getY());
    }

    @Test
    public void getWaitingPassengers() {
        assertEquals("A stop should be created with no passengers.",
                Collections.EMPTY_LIST, alphaStop.getWaitingPassengers());
        alphaStop.addPassenger(alec);
        alphaStop.addPassenger(bran);
        alphaStop.addPassenger(cato);
        assertEquals("get method should work correctly. " +
                        "The order of the passengers in the returned list " +
                        "should be the same as the order in which the " +
                        "passengers were added to the stop.",
                Arrays.asList(alec, bran, cato),
                alphaStop.getWaitingPassengers());
    }

    @Test
    public void getWaitingPassengersShallowCopy() {
        alphaStop.getWaitingPassengers().add(alec);
        assertEquals("Modifying the returned list should not " +
                        "result in changes to the internal state of the class.",
                initPassengers, alphaStop.getWaitingPassengers());
    }

    @Test
    public void getVehicles() {
        assertEquals("A stop should be created with no vehicles.",
                Collections.EMPTY_LIST, alphaStop.getVehicles());
        alphaStop.transportArrive(bus);
        alphaStop.transportArrive(train);
        alphaStop.transportArrive(ferry);
        assertTrue("get method should work correctly.",
                alphaStop.getVehicles().contains(bus));
        assertTrue("get method should work correctly.",
                alphaStop.getVehicles().contains(train));
        assertTrue("get method should work correctly.",
                alphaStop.getVehicles().contains(ferry));
    }

    @Test
    public void getVehiclesShallowCopy() {
        alphaStop.getVehicles().add(bus);
        assertEquals("Modifying the returned list should not " +
                        "result in changes to the internal state of the class.",
                initVehicles, alphaStop.getVehicles());
    }

    @Test
    public void getNeighbours() {
        alphaStop.addNeighbouringStop(betaStop);
        alphaStop.addNeighbouringStop(gammaStop);
        alphaStop.addNeighbouringStop(deltaStop);
        assertTrue("get method should work correctly.",
                alphaStop.getNeighbours().contains(betaStop));
        assertTrue("get method should work correctly.",
                alphaStop.getNeighbours().contains(gammaStop));
        assertTrue("get method should work correctly.",
                alphaStop.getNeighbours().contains(deltaStop));
    }

    @Test
    public void getNeighboursShallowCopy() {
        alphaStop.getNeighbours().add(betaStop);
        assertEquals("Modifying the returned list should not " +
                        "result in changes to the internal state of the class.",
                initNeighbours, alphaStop.getNeighbours());
    }

    @Test
    public void getRoutes() {
        assertEquals("A stop should be created with no routes.",
                Collections.EMPTY_LIST, alphaStop.getRoutes());
        alphaStop.addRoute(busRoute);
        alphaStop.addRoute(trainRoute);
        alphaStop.addRoute(ferryRoute);
        assertTrue("get method should work correctly.",
                alphaStop.getRoutes().contains(busRoute));
        assertTrue("get method should work correctly.",
                alphaStop.getRoutes().contains(trainRoute));
        assertTrue("get method should work correctly.",
                alphaStop.getRoutes().contains(ferryRoute));
    }

    @Test
    public void getRoutesShallowCopy() {
        alphaStop.getRoutes().add(busRoute);
        assertEquals("Modifying the returned list should not " +
                        "result in changes to the internal state of the class.",
                initRoutes, alphaStop.getRoutes());
    }

    @Test
    public void addPassenger() {
        alphaStop.addPassenger(null);
        assertEquals("If the given passenger is null," +
                        "it should not be added to the stop.",
                initPassengers, alphaStop.getWaitingPassengers());
    }

    @Test
    public void transportArriveNull() {
        alphaStop.transportArrive(null);
        assertEquals("If the given vehicle is null, do nothing.",
                initVehicles, alphaStop.getVehicles());
    }

    @Test
    public void transportArriveContain() {
        // given
        alphaStop.transportArrive(bus);
        initVehicles.add(bus);
        // when
        alphaStop.transportArrive(bus);
        // then
        assertEquals("If the given vehicle is already at this stop, " +
                        "do nothing.",
                initVehicles, alphaStop.getVehicles());
    }

    @Test
    public void transportArriveUnload() {
        // given
        try {
            bus.addPassenger(alec);
            bus.addPassenger(bran);
            bus.addPassenger(cato);
        } catch (OverCapacityException e) {
            e.printStackTrace();
        }
        // when
        alphaStop.transportArrive(bus);
        // then
        assertEquals("transportArrive should clear the passengers " +
                        "on transport.",
                Collections.EMPTY_LIST, bus.getPassengers());
    }

    @Test
    public void transportArriveDeliver() {
        // given
        try {
            bus.addPassenger(alec);
            bus.addPassenger(bran);
            bus.addPassenger(cato);
        } catch (OverCapacityException e) {
            e.printStackTrace();
        }
        // when
        alphaStop.transportArrive(bus);
        initPassengers.add(alec);
        initPassengers.add(bran);
        initPassengers.add(cato);
        // then
        assertTrue("transportArrive should place " +
                        "the passengers at the stop.",
                alphaStop.getWaitingPassengers().contains(alec));
        assertTrue("transportArrive should place " +
                        "the passengers at the stop.",
                alphaStop.getWaitingPassengers().contains(bran));
        assertTrue("transportArrive should place " +
                        "the passengers at the stop.",
                alphaStop.getWaitingPassengers().contains(cato));
    }

    @Test
    public void transportArrivePark() {
        alphaStop.transportArrive(bus);
        initVehicles.add(bus);
        assertEquals("transportArrive should record " +
                        "the vehicle itself at the stop.",
                initVehicles, alphaStop.getVehicles());
    }

    @Test
    public void transportDepartNotContain() {
        alphaStop.transportDepart(bus, betaStop);
        assertFalse("If the given vehicle is not at this stop, " +
                        "do nothing.",
                betaStop.getVehicles().contains(bus));
    }

    @Test
    public void transportDepartVehicleNull() {
        alphaStop.transportArrive(null);
        alphaStop.transportDepart(null, betaStop);
        assertFalse("If the vehicle is null, do nothing.",
                betaStop.getVehicles().contains(null));
    }

    @Test
    public void transportDepartStopNull() {
        alphaStop.transportArrive(bus);
        alphaStop.transportDepart(bus, null);
        assertTrue("If the next stop is null, do nothing.",
                alphaStop.getVehicles().contains(bus));
    }

    @Test
    public void transportDepart() {
        // given
        busRoute.addStop(alphaStop);
        busRoute.addStop(betaStop);
        alphaStop.transportArrive(bus);
        // when
        alphaStop.transportDepart(bus, betaStop);
        // then
        assertEquals("transportDepart should update the vehicle's " +
                        "location to be the next stop.",
                betaStop, bus.getCurrentStop());
    }

    @Test
    public void addNeighbouringStopNull() {
        alphaStop.addNeighbouringStop(null);
        assertEquals("If the given stop is null, " +
                        "it should not be added as a neighbour.",
                initNeighbours, alphaStop.getNeighbours());
    }

    @Test
    public void addNeighbouringStopContain() {
        alphaStop.addNeighbouringStop(betaStop);
        alphaStop.addNeighbouringStop(betaStop);
        initNeighbours.add(betaStop);
        assertEquals("If this stop is already recorded " +
                        "as a neighbour, it should not be added as a neighbour",
                initNeighbours, alphaStop.getNeighbours());
    }

    @Test
    public void addNeighbouringStop() {
        alphaStop.addNeighbouringStop(betaStop);
        initNeighbours.add(betaStop);
        assertEquals("addNeighbouringStop method " +
                        "should work correctly",
                initNeighbours, alphaStop.getNeighbours());
    }

    @Test
    public void addRouteNull() {
        alphaStop.addRoute(null);
        assertEquals("If the given route is null, " +
                        "it should not be added to the stop.",
                initRoutes, alphaStop.getRoutes());
    }

    @Test
    public void addRoute() {
        alphaStop.addRoute(busRoute);
        initRoutes.add(busRoute);
        assertEquals(initRoutes, alphaStop.getRoutes());
    }

    @Test
    public void distanceTo() {
        assertEquals(4, alphaStop.distanceTo(gammaStop));
        assertEquals(6, alphaStop.distanceTo(deltaStop));
    }

    @Test
    public void isAtStop() {
        alphaStop.transportArrive(bus);
        assertTrue(alphaStop.isAtStop(bus));
    }

    @Test
    public void toStringRep() {
        assertEquals("Alpha:0:0", alphaStop.toString());
    }

    @Test
    public void equalsParameters() {
        assertNotEquals("If two stops are considered equal, " +
                "they should have the same name, x-coordinate, y-coordinate.",
                alphaStop, deltaStop);
        assertNotEquals("If two stops are considered equal, " +
                "they should have the same name, x-coordinate, y-coordinate.",
                alphaStop, gammaStop);
        assertNotEquals("If two stops are considered equal, " +
                "they should have the same name, x-coordinate, y-coordinate.",
                alphaStop, betaStop);
        assertEquals("If two stops are considered equal, " +
                "they should have the same name, x-coordinate, y-coordinate.",
                alphaStop, alphaCopy);
    }

    @Test
    public void equalsRegardlessOfOrder() {
        alphaStop.addRoute(busRoute);
        alphaStop.addRoute(trainRoute);
        alphaStop.addRoute(ferryRoute);
        alphaCopy.addRoute(busRoute);
        alphaCopy.addRoute(ferryRoute);
        alphaCopy.addRoute(trainRoute);
        assertEquals(alphaStop, alphaCopy);
    }

    @Test
    public void equalsIgnoreDuplicates() {
        alphaStop.addRoute(busRoute);
        alphaStop.addRoute(trainRoute);
        alphaStop.addRoute(ferryRoute);
        alphaStop.addRoute(ferryRoute);
        alphaCopy.addRoute(busRoute);
        alphaCopy.addRoute(trainRoute);
        alphaCopy.addRoute(ferryRoute);
        assertEquals("Duplicates of routes do not need to be " +
                        "considered in determining equality.",
                alphaStop, alphaCopy);
    }

    @Test
    public void equals() {
        alphaStop.addRoute(busRoute);
        assertNotEquals("equals method should work correctly.",
                alphaStop, betaStop);
        alphaCopy.addRoute(busRoute);
        assertEquals("equals method should work correctly.",
                alphaStop, alphaCopy);
        alphaCopy.addRoute(trainRoute);
        assertNotEquals("equals method should work correctly.",
                alphaStop, betaStop);
    }

    @Test
    public void hashCodeTest() {
        assertEquals("hashCode method should work correctly.",
                alphaStop.hashCode(), alphaCopy.hashCode());
        assertNotEquals("hashCode method should work correctly.",
                alphaStop.hashCode(), betaStop.hashCode());
    }
}