import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Chris Loftus, Josh Smith
 * @version 1.3 (1st March 2016)
 */

public class Map {

    private ArrayList<Settlement> settlements;
    private ArrayList<Road> roads;

    /**
     * Default constructor, initialises lists.
     */
    public Map() {
        // INSERT CODE
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
        // STEP 5: INSERT CODE HERE
        if (getSettlement(newSettlement.getName()) != null) {
            System.out.println("ERROR: Settlement already exists on map.");
        } else {
            settlements.add(newSettlement);
        }
    }

    /**
     * Removes the first settlement with an identical name in the list of settlements.
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
     * Adds a road to the list of roads. Checks to make sure that an identical road does not exist and also
     * that there isn't an identical route on the map.
     * @param road The road to add.
     */
    public void addRoad(Road road) {
        if (roads.contains(road)) {
            System.out.println("ERROR: Road already exists on map.");
        }else {
            boolean noOtherConnectingRoads = true;
            for (Road r : road.getSourceSettlement().getAllRoads()) {
                // IF theres another road with the same source and destination.
                if (r.getAlternateSettlement(road.getSourceSettlement()) == road.getDestinationSettlement()) {
                    noOtherConnectingRoads = false;
                    System.out.println("ERROR: Another road exists with the same source and destination settlements.");
                    break;

                }
            }
            if (noOtherConnectingRoads) {
                roads.add(road);
            }
        }
    }

    public void removeRoad(Road r) {
        r.getSourceSettlement().delete(r);
        roads.remove(r);
    }

    /**
     * Finds a road with details matching those provided and returns it.
     * @param name The name of the road to be found.
     * @param source The name of the source destination of the road to be found.
     * @param dest The name of the destination destination of the road to be found.
     * @return Returns a Road with identical values as those passed. or null if one is not found.
     */
    public Road findRoad(String name, String source, String dest) {
        for (Road r : roads) {
            if (r.getName() == name) {
                if (r.getSourceSettlement().getName() == source &&
                        r.getDestinationSettlement().getName() == dest) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * Loads the map based on a file format defined by the example provided.
     *
     * @throws IOException Throws IO exception if either file is not found.
     */
    public void load() throws IOException {
        // STEP 6: INSERT CODE HERE
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
                settlements.add(new Settlement(nm, pop, SettlementType.valueOf(type)));
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
            try {
                if (source != null && dest != null) {
                    roads.add(new Road(nm, Classification.valueOf(type), source, dest, dist));
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
     * Saves the map data to settlements.txt and roads.txt.
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

}
