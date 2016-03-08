import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Chris Loftus, Josh Smith
 * @version 2.0 (4th March 2016)
 */

public class Map {

    private ArrayList<Settlement> settlements;
    private ArrayList<Road> roads;

    /**
     * Default constructor, initialises lists.
     */
    public Map() {
        settlements = new ArrayList<>();
        roads = new ArrayList<>();
    }

    /**
     * In this version we display the result of calling toString on the command
     * line. Future versions may display graphically
     */
    public void display() {
        System.out.println(toString());
    }

    /**
     * Adds a settlement to the Map.
     *
     * @param newSettlement The settlement to add.
     * @throws IllegalArgumentException
     */
    public void addSettlement(Settlement newSettlement) throws IllegalArgumentException {
        if (getSettlement(newSettlement.getName()) != null) {
            System.out.println("ERROR: Settlement already exists on map.");
        } else {
            settlements.add(newSettlement);
        }
    }

    /**
     * Removes the first settlement with an identical name in the list of settlements.
     *
     * @param name The name of the settlement to remove.
     */
    public void removeSettlement(String name) {
        for (Settlement s : settlements) {
            if (s.getName().equals(name)) {
                roads.removeAll(s.getAllRoads());
                s.deleteRoads();
                settlements.remove(s);
                // Stop looping
                break;
            }
        }
        System.out.println("Settlement not found!");
    }

    /**
     * Adds a road to the map after performing various checks on the road e.g. Checks if there is an identical
     * road in the system and also checks if there is already a road between the two settlements this new road connects.
     *
     * @param nm The name of the road.
     * @param classifier The classification of the road.
     * @param source The source settlement of the road.
     * @param dest The destination settlement of the road.
     * @param dist The distance that the road spans in miles.
     */
    public void addRoad(String nm, Classification classifier, Settlement source, Settlement dest, double dist) {
        // Checks that there isn't already a connecting road between these settlements.
        if (!connectingRoadExists(source, dest)) {
            // check if we already have a road with these deails.
            if (findRoad(nm, source.getName(), dest.getName()) == null) {
                roads.add(new Road(nm, classifier, source, dest, dist));
            } else {
                System.out.println("ERROR: Road already exists on map.");
            }
        }else {
            System.out.println("ERROR: Connecting road already exists.");
        }
    }

    /**
     * Totally removes a road from the map.
     * @param r  The road to remove.
     */
    public void removeRoad(Road r) {
        // Trigger the removal of the road from connected settlements.
        r.getSourceSettlement().disconnectRoad(r);
        // remove it from the map.
        roads.remove(r);
    }

    /**
     * Finds a road with details matching those provided and returns it.
     *
     * @param name   The name of the road to be found.
     * @param source The name of the source destination of the road to be found.
     * @param dest   The name of the destination destination of the road to be found.
     * @return Returns a Road with identical values as those passed. or null if one is not found.
     */
    public Road findRoad(String name, String source, String dest) {
        for (Road r : roads) {
            if (r.getName().equals(name)) {
                if (r.getSourceSettlement().getName().equals(source) &&
                        r.getDestinationSettlement().getName().equals(dest)) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * Finds the fastest route between two settlements and returns the route as a list of roads in order A -> B.
     *
     * @param A The starting settlement.
     * @param B The destination settlement.
     * @return Returns a list of roads which make up the route found between A and B. If null, no route found.
     */
    public ArrayList<Road> findRoute(Settlement A, Settlement B) {
        Dijkstras d = new Dijkstras(this, A);
        return d.shortestRoute(B);
    }

    /**
     * @return Returns all settlements.
     */
    public ArrayList<Settlement> getSettlements() {
        return settlements;
    }


    /**
     * Takes a string and finds a matching settlement on the map and returns the object of said map.
     *
     * @param name The name of the settlement.
     * @return The object of the found settlement.
     */
    public Settlement getSettlement(String name) {
        for (Settlement s : settlements) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * @return Returns all roads.
     */
    public ArrayList<Road> getRoads() {
        return roads;
    }

    /**
     * Loads the map based on a file format defined by the example provided.
     *
     * @throws IOException Throws IO exception if either file is not found.
     */
    public void load() throws IOException {
        // reset arraylist
        settlements.clear();

        // ---------------------*** Settlements ***---------------------------
        Scanner infile = new Scanner(new FileReader("settlements.txt"));
        int settlementTotal = 0;
        infile.useDelimiter("\r?\n|\r|:"); // newlines or colons
        settlementTotal = infile.nextInt();

        for (int i = 0; i < settlementTotal; i++) {
            String nm = infile.next();
            int pop = infile.nextInt();
            String type = infile.next();
            try {
                addSettlement(new Settlement(nm, pop, SettlementType.valueOf(type)));
            } catch (IllegalArgumentException iae) {
                System.out.println("Settlement: " + nm + " not added due to invalid type.");
            }
        }
        infile.close();
        // ------------------------*** Roads ***-----------------------------
        infile = new Scanner(new FileReader("roads.txt"));
        int roadTotal = 0;
        infile.useDelimiter("\r?\n|\r|:");
        roadTotal = infile.nextInt();
        infile.nextLine(); // Keep this here
        for (int i = 0; i < roadTotal; i++) {
            String nm;
            String type;
            double dist;
            Settlement source;
            Settlement dest;
            nm = infile.next();
            type = infile.next();
            dist = infile.nextDouble();
            // Take settlement strings and convert them into existing objects with the same names
            source = getSettlement(infile.next());
            dest = getSettlement(infile.next());
            // validate the data read from file.
            try {
                // Ensure that the source and destinations exist.
                if (source != null && dest != null) {
                    addRoad(nm, Classification.valueOf(type), source, dest, dist);
                } else {
                    System.out.println("Source or Destination not found on map.");
                }
            } catch (IllegalArgumentException iae) {
                System.out.println("Road not added, IAE exception was thrown.");
            }
        }
        infile.close();
    }


    /**
     * Saves the map data to settlements.txt and roads.txt.
     *
     * @throws IOException Can throw IO exception.
     */
    public void save() throws IOException {
        // STEP 6: INSERT CODE HERE
        // --------------------------*** Settlements *** --------------------------------

        // possibly create file if not found
        PrintWriter outfile = new PrintWriter(new FileWriter("settlements.txt"));
        // Print amount of settlements in file
        outfile.println(settlements.size());
        for (Settlement s : settlements) {
            outfile.println(s.getName() + ":" +
                    s.getPopulation() + ":" +
                    s.getKind().toString());
        }
        outfile.close();
        // --------------------------*** Roads *** --------------------------------
        outfile = new PrintWriter(new FileWriter("roads.txt"));
        // Print amount of roads in file
        outfile.println(roads.size());
        for (Road r : roads) {
            outfile.println(r.getName() + ":" +
                    r.getClassification().toString() + ":" +
                    r.getLength() + ":" +
                    r.getSourceSettlement().getName() + ":" +
                    r.getDestinationSettlement().getName());
        }
        outfile.close();
    }

    public String toString() {
        String result = "";
        result += "Map Settlements: \n";
        for (Settlement s : settlements) {
            result += s.toString();
        }
        result += "\nMap Roads: \n";
        for (Road r : roads) {
            result += "Road Name=" + r.getName() +
                    ", Classification=" + r.getClassification().toString() +
                    ", length=" + r.getLength() +
                    ", Source Settlement=" + r.getSourceSettlement().getName() +
                    ", Destination Settlement=" + r.getDestinationSettlement().getName() + "\n";
        }
        return result;
    }

    /**
     * Checks whether a road already exists within the system
     * @param s
     * @param d
     * @return
     */
    private boolean connectingRoadExists(Settlement s, Settlement d) {
        for (Road r : s.getAllRoads()) {
            // checks whether or not Road r has an alternate settlement equal to d
            if (r.getAlternateSettlement(s).equals(d)) {
                return true;
            }
        }
        return false;
    }


}
