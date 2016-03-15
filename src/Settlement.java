import java.util.ArrayList;

/**
 * Represents a road that is linked to two settlements: source and destination.
 *
 * @author Chris Loftus, Josh Smith
 * @version 3.0 (14th March 2016)
 */
public class Settlement {
    private String name;
    private int population;
    private SettlementType kind;
    private ArrayList<Road> roads;

    /**
     * Constructor to build a settlement
     *
     * @param nm The name of the settlement
     */
    public Settlement(String nm, SettlementType k) {
        this(); // Means call the other constructor
        name = nm;
        kind = k;
    }

    /**
     * Constructor for a settlement with name, population and settlement type.
     * @param nm Name of the settlement
     * @param pop Population of the Settlement
     * @param k Type of the Settlement
     */
    public Settlement(String nm, int pop, SettlementType k) {
        this();
        name = nm;
        kind = k;
        population = pop;
    }

    /**
     * Default constructor which instantiates the ArrayList roads.
     */
    public Settlement() {
        // INSERT CODE HERE
        roads = new ArrayList<>();

    }

    /**
     * The name of the settlement. Note that there is no way to change
     * the name once created.
     *
     * @return The name of the settlement.
     */
    public String getName() {
            return name;
    }

    /**
     * The current population
     *
     * @return The population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Change the population size
     *
     * @param size The new population size
     */
    public void setPopulation(int size) {
        this.population = size;
    }

    /**
     * The kind of settlement, e.g. village, town etc
     *
     * @return The kind of settlement
     */
    public SettlementType getKind() {
        return kind;
    }

    /**
     * The population has grown or the settlement has been granted a new status (e.g. city status)
     *
     * @param kind The new settlement kind
     */
    public void setKind(SettlementType kind) {
        this.kind = kind;
    }

    /**
     * Add a new road to the settlement.
     *
     * @param road The new road to add.
     * @throws IllegalArgumentException if the settlement already contains the road
     */
    public void add(Road road) throws IllegalArgumentException {
        // ADD CODE HERE
        roads.add(road);
    }

    /**
     * Returns a ArrayList of Roads that match the given name
     *
     * @param name The name of the road to find
     * @return An ArrayList of Road objects (will be a maximum of two
     * items for any settlements: e.g. A487 goes from Aber to
     * Penparcau and from Aber to Bow Street
     */
    public ArrayList<Road> findRoads(String name) {
        ArrayList<Road> roadsFound = new ArrayList<>();

        for (Road r : roads) {
            if (r.getName() == name) {
                roadsFound.add(r);
            }
        }

        return roadsFound;
    }

    /**
     * Finds a road which leads from this Settlement directly to a destination settlement.
     *
     * @param dest The destination settlement.
     * @return A road which connects this settlement to the destination settlement.
     */
    public Road getRoadTo(Settlement dest) {
        for (Road r : roads) {
            if (r.getAlternateSettlement(this).equals(dest)) {
                return r;
            }
        }
        // if nothing found
        return null;
    }

    /**
     * Deletes all the roads attached to this settlement. It will
     * also detach these roads from the settlements at the other end
     * of each road
     */
    public void deleteRoads() {
        for (Road r : roads) {
            this.disconnectRoad(r);
        }

        roads.clear();
    }

    /**
     * Deletes the given road attached to this settlement. It will
     * also detach this road from the settlement at the other end
     * of the road
     *
     * @param road The road to disconnectRoad.
     */
    public void disconnectRoad(Road road) {
        for (Road r : roads) {
            if (r.equals(road)) {
                // Delete the road from the other settlement.
                r.getAlternateSettlement(this).removeRoad(road);

                // Stop looping through
                break;
            }
        }
    }

    /**
     * Removes a given road from the list of roads connected to the settlement.
     * @param road A road to be removed.
     */
    public void removeRoad(Road road) {
        roads.remove(road);
    }

    /**
     * Returns a list of all the roads connected to this settlement
     *
     * @return The roads attached to this settlement
     */
    public ArrayList<Road> getAllRoads() {
        // Notice how we create a separate array list object
        // and return that instead of the roads. This is so
        // that we don't break encapsulation and data hiding.
        // If I returned roads, then the calling code could change
        // change it directly which would be dangerous
        ArrayList<Road> result = new ArrayList<>(roads);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Settlement other = (Settlement) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * The state of the settlement including information about
     * connected roads
     *
     * @return The data in the settlement object.
     */
    public String toString() {
        String result = "";
        result += "Settlement Name = " + this.getName() + "\n" +
                "Population = " + this.getPopulation() + "\n" +
                "Kind = " + this.getKind().toString() + "\n";

        if (roads.size() > 0) {
            result += "Roads = \n";
            for (Road r : this.getAllRoads()) {
                result += indent(1) + r.getName() + " connected to " + r.getAlternateSettlement(this).getName() + "\n";
            }
        } else {
            result += "There are no roads connected to this settlement.";
        }
        return result + "\n";
    }

    private String indent(int level) {
        String ret = "";
        for (int i = 0; i < level; i++) {
            ret += "  ";
        }
        return ret;
    }
}
