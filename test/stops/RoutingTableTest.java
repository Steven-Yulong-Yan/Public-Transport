package stops;

import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;
import routes.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RoutingTableTest {
    @Rule
    public TestName name = new TestName();

    @Rule
    public Timeout timeout = Timeout.seconds(1);

    class TestingRoute extends Route {
        public TestingRoute(String name, int routeNumber) {
            super(name, routeNumber);
        }

        @Override
        public String getType() {
            return "Westerosi";
        }
    }

    private Stop castleBlack;
    private Stop winterfell;
    private Stop harrenhal;
    private Stop kingsLanding;
    private Stop kingswood;
    private Stop highgarden;
    private Stop hornHill;
    private Stop dragonStone;
    private Stop stormsEnd;
    private Stop riverrun;
    private Stop casterlyRock;
    private Stop lannisport;
    private Stop eastwatch;

    private Stop braavos;

    @Before
    public void setUp() {

        castleBlack = new Stop("Castle Black", 0, 14);
        winterfell = new Stop("Winterfell", 0, 13);
        harrenhal = new Stop("Harrenhal", 0, 3);
        kingsLanding = new Stop("Kings Landing", 0, 0);
        kingswood = new Stop("Kingswood", 0, -1);
        highgarden = new Stop("Highgarden", 0, -4);
        hornHill = new Stop("Horn Hill", 0, -5);
        dragonStone = new Stop("Dragonstone", 3, 3);
        stormsEnd = new Stop("Storms End", 3, 0);
        riverrun = new Stop("Riverrun", -1, 3);
        casterlyRock = new Stop("Casterly Rock", -4, 0);
        lannisport = new Stop("Lannisport", -4, -1);
        eastwatch = new Stop("Eastwatch-by-the-Sea", 1, 14);

        braavos = new Stop("Braavos", 8, 8);

        Route kingsroad = new TestingRoute("Kingsroad", 1);
        Route goldRoad = new TestingRoute("Gold Road", 2);
        Route roseroad = new TestingRoute("Roseroad", 3);
        Route riverRoad = new TestingRoute("River Road", 4);
        Route theSea = new TestingRoute("The Narrow Sea", 5);
        Route theWall = new TestingRoute("The Wall", 6);

        kingsroad.addStop(castleBlack);
        kingsroad.addStop(winterfell);
        kingsroad.addStop(harrenhal);
        kingsroad.addStop(kingsLanding);

        goldRoad.addStop(casterlyRock);
        goldRoad.addStop(kingsLanding);
        goldRoad.addStop(stormsEnd);

        roseroad.addStop(kingsLanding);
        roseroad.addStop(kingswood);
        roseroad.addStop(highgarden);
        roseroad.addStop(hornHill);

        riverRoad.addStop(lannisport);
        riverRoad.addStop(casterlyRock);
        riverRoad.addStop(riverrun);
        riverRoad.addStop(harrenhal);

        theSea.addStop(winterfell);
        theSea.addStop(dragonStone);
        theSea.addStop(kingsLanding);

        theWall.addStop(castleBlack);
        theWall.addStop(eastwatch);

    }

    @After
    public void tearDown() {
        System.out.println("Test method '" + name.getMethodName() +
                "' has been run");
    }

    @Test
    public void getStop() {
        assertEquals(kingsLanding, kingsLanding.getRoutingTable().getStop());
    }

    @Test
    public void costTo() {
        assertEquals(0, kingsLanding.getRoutingTable().costTo(kingsLanding));
        assertEquals(3, kingsLanding.getRoutingTable().costTo(harrenhal));
        assertEquals(4, kingsLanding.getRoutingTable().costTo(riverrun));
        assertEquals(4, kingsLanding.getRoutingTable().costTo(casterlyRock));
        assertEquals(5, kingsLanding.getRoutingTable().costTo(lannisport));
        assertEquals(3, kingsLanding.getRoutingTable().costTo(stormsEnd));
        assertEquals(6, kingsLanding.getRoutingTable().costTo(dragonStone));
        assertEquals(13, kingsLanding.getRoutingTable().costTo(winterfell));
        assertEquals(14, kingsLanding.getRoutingTable().costTo(castleBlack));
        assertEquals(15, kingsLanding.getRoutingTable().costTo(eastwatch));
        assertEquals(1, kingsLanding.getRoutingTable().costTo(kingswood));
        assertEquals(4, kingsLanding.getRoutingTable().costTo(highgarden));
        assertEquals(5, kingsLanding.getRoutingTable().costTo(hornHill));
        assertEquals(Integer.MAX_VALUE, kingsLanding.getRoutingTable().costTo(braavos));
    }

    @Test
    public void getCosts() {
        Map<Stop, Integer> map = new HashMap<>();
        map.put(eastwatch, 15);
        map.put(castleBlack, 14);
        map.put(winterfell, 13);
        map.put(harrenhal, 3);
        map.put(kingsLanding, 0);
        map.put(kingswood, 1);
        map.put(highgarden, 4);
        map.put(hornHill, 5);
        map.put(dragonStone, 6);
        map.put(stormsEnd, 3);
        map.put(riverrun, 4);
        map.put(casterlyRock, 4);
        map.put(lannisport, 5);
        assertEquals(map, kingsLanding.getRoutingTable().getCosts());
    }

    @Test
    public void addOrUpdateEntry() {
        Stop oldtown = new Stop("Oldtown", 0, -6);
        assertTrue(hornHill.getRoutingTable().addOrUpdateEntry(oldtown, 3, highgarden));
        assertEquals(3, hornHill.getRoutingTable().costTo(oldtown));
        assertTrue(hornHill.getRoutingTable().addOrUpdateEntry(oldtown, 1, oldtown));
        assertEquals(1, hornHill.getRoutingTable().costTo(oldtown));
        assertFalse(hornHill.getRoutingTable().addOrUpdateEntry(oldtown, 2, oldtown));
        assertEquals(1, hornHill.getRoutingTable().costTo(oldtown));
    }

    @Test
    public void nextStop() {
        assertEquals(kingsLanding, kingsLanding.getRoutingTable().nextStop(kingsLanding));
        assertEquals(harrenhal, kingsLanding.getRoutingTable().nextStop(winterfell));
        assertEquals(harrenhal, kingsLanding.getRoutingTable().nextStop(castleBlack));
        assertEquals(harrenhal, kingsLanding.getRoutingTable().nextStop(eastwatch));
        assertEquals(harrenhal, kingsLanding.getRoutingTable().nextStop(harrenhal));
        assertEquals(dragonStone, kingsLanding.getRoutingTable().nextStop(dragonStone));
        assertEquals(stormsEnd, kingsLanding.getRoutingTable().nextStop(stormsEnd));
        assertEquals(harrenhal, kingsLanding.getRoutingTable().nextStop(riverrun));
        assertEquals(casterlyRock, kingsLanding.getRoutingTable().nextStop(casterlyRock));
        assertEquals(casterlyRock, kingsLanding.getRoutingTable().nextStop(lannisport));
        assertEquals(kingswood, kingsLanding.getRoutingTable().nextStop(kingswood));
        assertEquals(kingswood, kingsLanding.getRoutingTable().nextStop(highgarden));
        assertEquals(kingswood, kingsLanding.getRoutingTable().nextStop(hornHill));
        assertNull(kingsLanding.getRoutingTable().nextStop(braavos));
    }

    @Test
    public void traverseNetworkOrderUnspecificAllowDuplicates() {
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(kingsLanding));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(dragonStone));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(winterfell));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(harrenhal));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(riverrun));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(casterlyRock));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(lannisport));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(castleBlack));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(eastwatch));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(kingswood));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(highgarden));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(hornHill));
        assertTrue(kingsLanding.getRoutingTable().traverseNetwork().contains(stormsEnd));
    }

    @Test
    public void traverseNetworkOrderSpecificDisallowDuplicates() {
        List<Stop> stops = new ArrayList<>();
        stops.add(kingsLanding);
        stops.add(dragonStone);
        stops.add(winterfell);
        stops.add(harrenhal);
        stops.add(riverrun);
        stops.add(casterlyRock);
        stops.add(lannisport);
        stops.add(castleBlack);
        stops.add(eastwatch);
        stops.add(kingswood);
        stops.add(highgarden);
        stops.add(hornHill);
        stops.add(stormsEnd);
        assertEquals(stops, kingsLanding.getRoutingTable().traverseNetwork());
    }

    @Test
    public void transferEntries() {
        assertFalse(kingsLanding.getRoutingTable().transferEntries(braavos));
        assertFalse(kingsLanding.getRoutingTable().transferEntries(dragonStone));
    }

    @Test
    public void addNeighbourSynchroniseTransferEntries() {
        assertEquals(casterlyRock, lannisport.getRoutingTable().nextStop(highgarden));
        assertEquals(9, lannisport.getRoutingTable().costTo(highgarden));
        assertEquals(9, highgarden.getRoutingTable().costTo(lannisport));
        lannisport.addNeighbouringStop(highgarden);
        assertEquals(highgarden, lannisport.getRoutingTable().nextStop(highgarden));
        assertEquals(7, lannisport.getRoutingTable().costTo(highgarden));
        assertEquals(7, highgarden.getRoutingTable().costTo(lannisport));

        assertEquals(kingsLanding, dragonStone.getRoutingTable().nextStop(harrenhal));
        assertEquals(9, dragonStone.getRoutingTable().costTo(harrenhal));
        assertEquals(9, harrenhal.getRoutingTable().costTo(dragonStone));
        assertEquals(kingsLanding, dragonStone.getRoutingTable().nextStop(riverrun));
        assertEquals(10, dragonStone.getRoutingTable().costTo(riverrun));
        assertEquals(10, riverrun.getRoutingTable().costTo(dragonStone));
        dragonStone.addNeighbouringStop(harrenhal);
        harrenhal.addNeighbouringStop(dragonStone);
        assertEquals(harrenhal, dragonStone.getRoutingTable().nextStop(harrenhal));
        assertEquals(3, dragonStone.getRoutingTable().costTo(harrenhal));
        assertEquals(3, harrenhal.getRoutingTable().costTo(dragonStone));
        assertEquals(harrenhal, dragonStone.getRoutingTable().nextStop(riverrun));
        assertEquals(4, dragonStone.getRoutingTable().costTo(riverrun));
        assertEquals(4, riverrun.getRoutingTable().costTo(dragonStone));
    }
}