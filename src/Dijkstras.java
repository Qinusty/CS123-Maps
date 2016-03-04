import java.lang.reflect.Array;
import java.util.*;


/**
 * @author Josh Smith
 * @version 2.0 (4th March 2016)
 */
public class Dijkstras {

    private ArrayList<Road> edges;
    private ArrayList<Settlement> vertices;
    private Settlement source;
    private Map m;

    /**
     * Constructor for the Dijkstras class. Initialises the edges and vertices with map roads and settlements.
     *
     * @param m      The Map to be operated on.
     * @param source The Source settlement.
     */
    public Dijkstras(Map m, Settlement source) {
        this.edges = new ArrayList<>(m.getRoads()); // take new copies of the list
        this.vertices = new ArrayList<>(m.getSettlements()); // take new copies of the list
        this.source = source;
        this.m = m;
    }

    /**
     * Calculates the shortest route betweeen the source Settlement and the passed destination
     * Settlement.
     *
     * @param dest The destination settlement to be routed towards.
     * @return An ArrayList of roads in the order of the route.
     */
    public ArrayList<Road> shortestRoute(Settlement dest) {
        // optimal distances from source
        HashMap<Settlement, Double> dist = new HashMap<>();
        // previous nodes in the routes from source.
        HashMap<Settlement, Settlement> prev = new HashMap<>();

        // Q is the identifier commonly used to represent the unvisited vertices on the graph.
        ArrayList<Settlement> Q = new ArrayList<>(vertices);

        for (Settlement v : vertices) {
            dist.put(v, (double) 2000000); // 2000000 is 'infinity'
            prev.put(v, null); // null is undefined
        }
        dist.put(source, (double) 0);
        // Start at source
        Settlement u;
        // This is the first loop
        boolean firstLoop = true;
        while (!Q.isEmpty()) {
            if (firstLoop) {
                u = source;
                firstLoop = false;
            } else {
                u = minDist(Q, dist);
            }
            Q.remove(u);

            for (Road e : u.getAllRoads()) { // check each road connected to u
                // get the settlement at the other end of road e
                // otherSettlement, Also known as v in terms of mathematical representation of the algorithm
                Settlement otherSettlement = e.getAlternateSettlement(u);
                Double alt = (double) 0;
                if (Q.contains(otherSettlement)) { // if this road leads to a settlement we haven't been to.
                    // Set the possible alt distance to this other settlement to
                    // the distance to u + this current roads length
                    alt = dist.get(u) + e.getLength();
                    // Check if this route to otherSettlement is faster than the one we already know about
                    if (alt < dist.get(otherSettlement)) {
                        // set the new, shorter distance to otherSettlement to alt
                        dist.put(otherSettlement, alt);
                        // set the new previous node for otherSettlement to u
                        prev.put(otherSettlement, u);
                    }
                }
            }
        }
        return grabRoute(prev, dest);
    }

    /**
     * Takes a distance calculated hashMap of previous Settlements and the destination and returns a route in form of
     * an ArrayList of type Road.
     *
     * @param prev HashMap of previous nodes leading back towards the source settlement.
     * @param dest The destination to start backtracking from.
     * @return Returns an ArrayList of type Road storing the Roads to take for the desired route.
     */
    private ArrayList<Road> grabRoute(HashMap<Settlement, Settlement> prev, Settlement dest) {
        ArrayList<Road> ret = new ArrayList<>();
        // In Dijkstra's we have to work backwards once we have the shortest routes calculated.
        while (dest != source) {
            // Add the road between Dest and the previous road from dest on the shortest path. (prev)
            ret.add(dest.getRoadTo(prev.get(dest)));
            dest = prev.get(dest);
        }
        // This puts the roads in appropriate order for the route.
        Collections.reverse(ret);
        return ret;
    }

    /**
     * Gets a settlement which has the smallest possible distance from source via known settlements,
     * which is also unvisited.
     *
     * @param Q    The list of unvisited settlements.
     * @param dist The HashMap of already calculated distances from the source destination.
     * @return Returns a Settlement which is unvisited and has the minimal distance from source.
     */
    private Settlement minDist(ArrayList<Settlement> Q, HashMap<Settlement, Double> dist) {
        Settlement ret = null;
        double curMin = dist.get(Q.get(0)).doubleValue();

        for (Settlement v : Q) {
            if (dist.get(v).doubleValue() <= curMin) {
                curMin = dist.get(v).doubleValue();
                ret = v;
            }
        }
        return ret;
    }


}
