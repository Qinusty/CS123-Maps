

import java.util.Scanner;

/**
 * Main class to test the Road and Settlement classes
 * 
 * @author Chris Loftus, Josh Smith (add your name and change version number/date)
 * @version 1.1 (26th February 2016)
 *
 */
public class Application {

	private Scanner scan;
	private Map map;

	public Application() {
		scan = new Scanner(System.in);
		map = new Map();
	}

	private void runMenu() {
		// STEP 1: ADD MENU CODE HERE
		char input;
		do {
			printMenu();
			input = askChar("Enter Choice");
			switch(input) {
				case '1':
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
					break;
				case '7':
					break;
				case 'Q':
					break;
			}
		}while(input != 'Q');

	}

	// STEP 1: ADD PRIVATE UTILITY MENTHODS HERE. askForRoadClassifier, save and load provided
	
	private Classification askForRoadClassifier() {
		Classification result = null;
		boolean valid;
		do{
			valid = false;
			System.out.print("Enter a road classification: ");
			for (Classification cls: Classification.values()){
				System.out.print(cls + " ");
			}
			String choice = scan.nextLine().toUpperCase();
			try {
				result = Classification.valueOf(choice);
				valid = true;
			} catch (IllegalArgumentException iae){
				System.out.println(choice + " is not one of the options. Try again.");
			}
		}while(!valid);
		return result;
	}
	
	private void save() {
		map.save();
	}

	private void load() {
		map.load();
	}

	private void printMenu() {
		// STEP 1: ADD CODE HERE
		System.out.println("--* Menu *--");
		System.out.println( " 1) Create Settlement\n" +
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
	 * @param query
	 * 		The query to be displayed to the user to prompt the input.
	 * @return
	 * 		returns the character the user inputted.
     */
	private char askChar(String query) {
		System.out.print(query + ": ");
		return scan.nextLine().charAt(0);
	}

	public static void main(String args[]) {
		Application app = new Application();
		app.load();
		app.runMenu();
		app.save();
	}

}
