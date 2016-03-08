import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class to test the Road and Settlement classes
 *
 * @author Chris Loftus, Josh Smith
 * @version 2.0 (4th March 2016)
 */
public class Application {

    private Scanner scan;
    private Map map;

    /**
     * Default constructor which instantiates the scanner and Map.
     */
    public Application() {
        scan = new Scanner(System.in);
        map = new Map();
    }

    /**
     * Runs the menu, this function loops until the user inputs Q into the menu options.
     */
    private void runMenu() {
        char input;
        do {
            printMenu();
            input = askChar("Enter Choice");
            switch (input) {
                case '1': // Add Settlement
                    map.addSettlement(askForSettlement());
                    break;
                case '2': // Remove Settlement
                    System.out.print("Settlement to remove: ");
                    map.removeSettlement(scan.nextLine());
                    break;
                case '3': // Add Road
                    askForRoad();
                    break;
                case '4': // Remove Road
                    Road removalRoad = askForRoadToRemove();
                    if (removalRoad == null) {
                        System.out.println("ERROR: Road not found.");
                    } else {
                        map.removeRoad(removalRoad);
                    }
                    break;
                case '5': // Display Map
                    map.display();
                    break;
                case '6':
                    // shortest route query
                    queryForRoute();
                    break;
                case '7': // Save Map to file
                    save();
                    break;
                case '8': // Load Map from file
                    load();
                    break;
            }
        } while (input != 'q' && input != 'Q');

    }

    // STEP 1: ADD PRIVATE UTILITY MENTHODS HERE. askForRoadClassifier, save and load provided

    /**
     * Queries the user for a road classification.
     *
     * @return Returns a road classification.
     */
    private Classification askForRoadClassifier() {
        Classification result = null;
        boolean valid;
        do {
            valid = false;
            System.out.print("Enter a road classification: ");
            for (Classification cls : Classification.values()) {
                System.out.print(cls + " ");
            }
            String choice = scan.next().toUpperCase();
            try {
                result = Classification.valueOf(choice);
                valid = true;
            } catch (IllegalArgumentException iae) {
                System.out.println(choice + " is not one of the options. Try again.");
            }
        } while (!valid);
        scan.nextLine();
        return result;
    }

    /**
     * Queries the user for a settlement type.
     *
     * @return Returns a settlement type.
     */
    private SettlementType askForSettlementType() {
        SettlementType type = null;
        boolean valid;
        do {
            valid = false;
            System.out.print("Type: ");
            String choice = scan.nextLine().toUpperCase();
            try {
                type = SettlementType.valueOf(choice);
                valid = true;
            } catch (IllegalArgumentException iae) {
                System.out.print(choice + " is not a valid choice. Try again. (");
                for (int i = 0; i < SettlementType.values().length; i++) {
                    System.out.print(SettlementType.values()[i]);

                    if (i < SettlementType.values().length - 1) { // if not the last element in the array.
                        System.out.print(", "); // add a seperator.
                    }
                }
                System.out.println(")");
            }
        } while (!valid);
        return type;
    }

    /**
     * Queries the user for data required to find the shortest route between two settlements
     * and then prints the route to the screen using printRoute();
     */
    private void queryForRoute() {
        System.out.println("Please enter the information required to find your route.");
        Settlement source = null;
        Settlement dest = null;
        boolean valid = false;
        while (!valid) {
            System.out.print("Source: ");
            source = map.getSettlement(scan.nextLine());
            if (source == null) {
                System.out.println("ERROR: Settlement not found, try again!");
            } else {
                valid = true;
            }
        }
        // Reset validity.
        valid = false;
        while (!valid) {
            System.out.print("Destination: ");
            dest = map.getSettlement(scan.nextLine());
            if (dest == null) {
                System.out.println("ERROR: Settlement not found, try again!");
            } else {
                valid = true;
            }
        }
        // Gets map to find the shortest route and prints it to the user.
        printRoute(map.findRoute(source, dest), source);
    }

    /**
     * Prints the route in a user friendly manner.
     *
     * @param route  The route which has been pre-calculated.
     * @param source The source destination.
     */
    private void printRoute(ArrayList<Road> route, Settlement source) {
        Settlement next;
        Settlement current = source;
        double totalMiles = 0;
        System.out.println("Starting at " + source.getName());
        for (Road r : route) {
            next = r.getAlternateSettlement(current);
            System.out.println("Take the " + r.getName() + " for " + r.getLength() + " miles until you reach " +
                    next.getName());
            totalMiles += r.getLength();
            current = next;
        }
        DecimalFormat decimal = new DecimalFormat("##.00");
        System.out.println("The total mileage of the route is : " + decimal.format(totalMiles) + " miles.");
    }

    /**
     * Queries the user for details to create a road.
     *
     * @return A Road object instantiated with the variables received when querying the user.
     */
    private void askForRoad() {
        String name;
        Classification classification;
        double dist;
        Settlement source = null, dest = null;
        System.out.print("Enter road name: ");
        name = scan.nextLine();
        classification = askForRoadClassifier();

        // VALIDATE map.getSettlement()
        while(source == null) {
            System.out.print("Enter source settlement: ");
            source = map.getSettlement(scan.nextLine());
            if (source == null) {
                System.out.println("Invalid name of settlement.");
            }
        }
        while (dest == null) {
            System.out.print("Enter destination settlement: ");
            dest = map.getSettlement(scan.nextLine());
            if (dest == null) {
                System.out.println("Invalid name of settlement.");
            }
        }

        System.out.print("Distance (Miles) : ");
        dist = scan.nextDouble();
        scan.nextLine(); // keep

        map.addRoad(name, classification, source, dest, dist);
    }

    private Road askForRoadToRemove() {
        String nm, source, dest;

        System.out.print("Name: ");
        nm = scan.nextLine();
        System.out.print("Source Settlement: ");
        source = scan.nextLine();
        System.out.print("Destination Settlement: ");
        dest = scan.nextLine();

        return map.findRoad(nm, source, dest);
    }

    /**
     * Saves the map state to two files, roads.txt and settlements.txt.
     */
    private void save() {
        try {
            map.save();
            System.out.println("Save Complete!");
        } catch (IOException IO) {
            System.out.println("IO Exception, File probably not found.");
        }
    }

    /**
     * Loads the map state from roads.txt and settlements.txt.
     */
    private void load() {
        try {
            map.load();
            System.out.println("Load Complete!");
        } catch (IOException IO) {
            System.out.println("IO Exception, File not found.");
        }

    }

    /**
     * Prints the multiline menu to screen.
     */
    private void printMenu() {
        // STEP 1: ADD CODE HERE
        System.out.println("--* Menu *--");
        System.out.println(" 1) Create Settlement\n" +
                " 2) Delete Settlement\n" +
                " 3) Create Road\n" +
                " 4) Delete Road\n" +
                " 5) Display Map\n" +
                " 6) Get shortest route\n" +
                " 7) Save Map\n" +
                " 8) Load Map\n" +
                " Q) Quit"
        );
    }

    /**
     * Prints a single line query and retrieves a character input from the user.
     *
     * @param query The query to be displayed to the user to prompt the input.
     * @return returns the character the user inputted.
     */
    private char askChar(String query) {
        System.out.print(query + ": ");
        return scan.nextLine().charAt(0);
    }

    /**
     * creates a settlement based on the users input and returns the settlement object.
     *
     * @return A settlement created from user input;
     */
    private Settlement askForSettlement() {
        String nm = "";
        int pop = 0;
        SettlementType type = null;
        System.out.println("New Settlement");
        System.out.print("Name: ");
        nm = scan.nextLine();
        System.out.print("Population: ");
        pop = scan.nextInt();

        // THIS IS NECESSARY, scan.nextInt() DOESNT READ THE \n
        scan.nextLine();

        type = askForSettlementType();
        return new Settlement(nm, pop, type);
    }

    /* -------------------_Static Stuff_----------------------------- */

    public static void main(String args[]) throws IOException {
        Application app = new Application();
        app.load();
        app.runMenu();
        app.save();
    }

}
