package stops;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class should map destination stops to RoutingEntry objects.
 *
 * <p>The table is able to redirect passengers from their current stop to
 * the next intermediate stop which they should go to in order to reach
 * their final destination.</p>
 */
public class RoutingTable {

    // the stop for which this table will handle routing
    private Stop initialStop;

    // the map for destination stops and route entries
    private Map<Stop, RoutingEntry> map;

    /**
     * Creates a new RoutingTable for the given stop.
     *
     * <p>The routing table should be created with an entry for its
     * initial stop (i.e. a mapping from the stop to a
     * {@link RoutingEntry#RoutingEntry()} for that stop with a cost of
     * zero.</p>
     *
     * @param initialStop The stop for which this table will handle routing.
     */
    public RoutingTable(Stop initialStop) {
        this.initialStop = initialStop;
        this.map = new ConcurrentHashMap<>();
        this.map.put(this.getStop(), new RoutingEntry(this.getStop(), 0));
    }

    /**
     * Return the stop for which this table will handle routing.
     *
     * @return the stop for which this table will handle routing
     */
    public Stop getStop() {
        return initialStop;
    }

    /**
     * Returns the cost associated with getting to the given stop.
     *
     * @param stop The stop to get the cost.
     *
     * @return The cost to the given stop, or Integer.MAX_VALUE if
     * the stop is not currently in this routing table.
     */
    public int costTo(Stop stop) {
        for (Map.Entry<Stop, RoutingEntry> entry : this.map.entrySet()) {
            if (entry.getKey() == stop) {
                return entry.getValue().getCost();
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Maps each destination stop in this table to the cost
     * associated with getting to that destination.
     *
     * @return A mapping from destination stops to the costs
     * associated with getting to those stops.
     */
    public Map<Stop, Integer> getCosts() {
        Map<Stop, Integer> map = new ConcurrentHashMap<>();
        this.map.forEach((destination, routingEntry) ->
                map.put(destination, routingEntry.getCost()));
        return map;
    }

    /**
     * If there is currently no entry for the destination in the table,
     * a new entry for the given destination should be added, with
     * a RoutingEntry for the given cost and next (intermediate) stop.
     *
     * <p>If there is already an entry for the given destination,
     * and the newCost is lower than the current cost associated with
     * the destination, then the entry should be updated to have
     * the given newCost and next (intermediate) stop.</p>
     *
     * <p>If there is already an entry for the given destination,
     * but the newCost is greater than or equal to the current cost associated
     * with the destination, then the entry should remain unchanged.</p>
     *
     * @param destination The destination stop to add/update the entry.
     * @param newCost The new cost to associate with the new/updated entry.
     * @param intermediate The new intermediate/next stop to associate with
     *                     the new/updated entry.
     *
     * @return True if a new entry was added, or an existing one was updated,
     * or false if the table remained unchanged.
     */
    public boolean addOrUpdateEntry(Stop destination, int newCost,
            Stop intermediate) {
        // ( ... ) || (... && ...)
        if ((!this.map.containsKey(destination)) ||
                (this.map.containsKey(destination) &&
                        newCost < costTo(destination))) {
            this.map.put(destination, new RoutingEntry(intermediate, newCost));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the next intermediate stop which passengers should be routed to
     * in order to reach the given destination. If the given stop is null
     * or not in the table, then return null.
     *
     * @param destination The destination which the passengers are being routed.
     *
     * @return The best stop to route the passengers to in order to
     * reach the given destination.
     */
    public Stop nextStop(Stop destination) {
        for (Map.Entry<Stop, RoutingEntry> entry : this.map.entrySet()) {
            if (entry.getKey() == destination) {
                return entry.getValue().getNext();
            }
        }
        return null;
    }

    /**
     * Performs a traversal of all the stops in the network, and returns a list
     * of every stop which is reachable from the stop stored in this table.
     *
     * <ol>
     *     <li>Firstly create an empty list of Stops and an empty Stack of
     *     Stops. Push the RoutingTable's Stop on to the stack.</li>
     *     <li>While the stack is not empty,
     *     <ol>
     *         <li>pop the top Stop (current) from the stack.</li>
     *         <li>For each of that stop's neighbours,
     *         <ol>
     *             <li>if they are not in the list, add them to the stack.</li>
     *         </ol>
     *         </li>
     *         <li>Then add the current Stop to the list.</li>
     *     </ol>
     *     </li>
     *     <li>Return the list of seen stops.</li>
     * </ol>
     *
     * @return All of the stops in the network which are reachable by the stop
     * stored in this table.
     */
    public List<Stop> traverseNetwork() {
        List<Stop> stopList = new ArrayList<>();
        Stack<Stop> stopStack = new Stack<>();
        stopStack.push(this.getStop());
        while (!stopStack.empty()) {
            Stop current = stopStack.pop();
            current.getNeighbours().forEach(stop -> {
                if (!stopList.contains(stop)) {
                    stopStack.push(stop);
                }
            });
            // Check if the stop is already in the list (not in Javadoc)
            if (!stopList.contains(current)) {
                stopList.add(current);
            }
        }
        return stopList;
    }

    /**
     * Updates the entries in the routing table of the given other stop,
     * with the entries from this routing table.
     *
     * <p>If this routing table has entries which the other stop's table
     * doesn't, then the entries should be added to the other table
     * (as defined in {@link #addOrUpdateEntry(Stop, int, Stop)}) with the cost
     * being updated to include the distance.</p>
     *
     * <p>If this routing table has entries which the other stop's table does
     * have, and the new cost would be lower than that associated with its
     * existing entry, then the other table's entry should be updated
     * (as defined in {@link #addOrUpdateEntry(Stop, int, Stop)}).</p>
     *
     * <p>If this routing table has entries which the other stop's table does
     * have, but the new cost would be greater than or equal to that associated
     * with its existing entry, then the other table's entry should remain
     * unchanged.</p>
     *
     * @param other The stop whose routing table this table's entries should
     *              be transferred.
     *
     * @return True if any new entries were added to the other stop's table,
     * or if any of its existing entries were updated, or false if the other
     * stop's table remains unchanged.
     */
    public boolean transferEntries(Stop other) {
        if (!this.getStop().getNeighbours().contains(other)) {
            return false;
        }
        boolean transferred = false;
        for (Map.Entry<Stop, RoutingEntry> entry : this.map.entrySet()) {
            // ( ... ) || (... && ...)
            if (!other.getRoutingTable().map.containsKey(entry.getKey()) ||
                    (other.getRoutingTable().map.containsKey(entry.getKey()) &&
                            (this.costTo(other) + this.costTo(entry.getKey())) <
                            other.getRoutingTable().costTo(entry.getKey()))) {
                other.getRoutingTable().addOrUpdateEntry(entry.getKey(),
                        this.costTo(other) +
                                this.costTo(entry.getKey()),
                        this.getStop());
                transferred = true;
            }
        }
        return transferred;
    }

    /**
     * Synchronises this routing table with the other tables in the network.
     *
     * <p>In each iteration, every stop in the network which is reachable by
     * this table's stop (as returned by traverseNetwork()) must be considered.
     * For each stop x in the network, each of its neighbours must be visited,
     * and the entries from x must be transferred to each neighbour
     * (using the {@link #transferEntries(Stop)} method).</p>
     *
     * <p>If any of these transfers results in a change to the table that the
     * entries are being transferred, then the entire process must be repeated
     * again. These iterations should continue happening until no changes occur
     * to any of the tables in the network.</p>
     *
     * <p>This process is designed to handle changes which need to be
     * propagated throughout the entire network, which could take more than
     * one iteration.</p>
     */
    public void synchronise() {
        // Depth-first search
        this.traverseNetwork().forEach(stop ->
                stop.getNeighbours().forEach(neighbour -> {
            if (stop.getRoutingTable().transferEntries(neighbour)) {
                this.synchronise();
            }
        }));
    }

    /**
     * Adds the given stop as a neighbour of the stop stored in this table.
     *
     * <p>A neighbouring stop should be added as a destination in this table,
     * with the cost to reach that destination simply being the Manhattan
     * distance between this table's stop and the given neighbour stop.</p>
     *
     * <p>If the given neighbour already exists in the table, it should be
     * updated (as defined in {@link #addOrUpdateEntry(Stop, int, Stop)}).</p>
     *
     * <p>The 'intermediate'/'next' stop between this table's stop and the
     * new neighbour stop should simply be the neighbour stop itself.</p>
     *
     * <p>Once the new neighbour has been added as an entry, this table should
     * be synchronised with the rest of the network using the synchronise()
     * method.</p>
     *
     * @param neighbour The stop to be added as a neighbour.
     */
    public void addNeighbour(Stop neighbour) {
        // The following line of code can be omitted in practical uses
        this.getStop().addNeighbouringStop(neighbour);
        this.addOrUpdateEntry(neighbour, this.getStop().distanceTo(neighbour),
                neighbour);
        this.synchronise();
    }
}