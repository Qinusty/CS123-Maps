import java.io.IOException;
import java.util.Scanner;

/**
 * Main class to test the Road and Settlement classes
 *
 * @author Chris Loftus, Josh Smith
 * @version 1.3 (29th February 2016)
 */
public class Application {

    private Scanner scan;
    private Map map;

    /**
     *  Default constructor which instantiates the scanner and Map.
     */
    public Application() {
        scan = new Scanner(System.in);
        map = new Map();
    }

    /**
     * Runs the menu, this function loops until the user inputs Q into the menu options.
     */
    private void runMenu() {
        // STEP 1: ADD MENU CODE HERE
        char input;
        do {
            printMenu();
            input = askChar("Enter Choice");
            switch (input) {
                case '1':
                    map.addSettlement(askForSettlement());
                    break;
                case '2':
                    break;
                case '3':
                    break;
                case '4':
                    break;
                case '5':
                    break;
                case '6':
                    save();
                    break;
                case '7':
                    load();
                    break;
            }
        } while (input != 'q' || input != 'Q');

    }

    // STEP 1: ADD PRIVATE UTILITY MENTHODS HERE. askForRoadClassifier, save and load provided

    /**
     * Queries the user for a road classification.
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
            String choice = scan.nextLine().toUpperCase();
            try {
                result = Classification.valueOf(choice);
                valid = true;
            } catch (IllegalArgumentException iae) {
                System.out.println(choice + " is not one of the options. Try again.");
            }
        } while (!valid);
        return result;
    }

    /**
     * Saves the map state to two files, roads.txt and settlements.txt.
     */
    private void save(){
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
            System.out.println("IO Exception, File probably not found.");
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
                " 6) Save Map\n" +
                " 7) Load Map\n" +
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
        scan.nextLine(); // DONT TOUCH
        // DONT TOUCH

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
        return new Settlement(nm, pop, type);
    }

    public static void main(String args[]) throws IOException{
        Application app = new Application();
        app.load();
        app.runMenu();
        app.save();
    }

}
