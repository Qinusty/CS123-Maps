import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Chris Loftus, Josh Smith
 * @version 1.1 (28th February 2016)
 *
 */

public class Map {

	private ArrayList<Settlement> settlements;
	private ArrayList<Road> roads;

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

	public void addSettlement(Settlement newSettlement) throws IllegalArgumentException {
		// STEP 5: INSERT CODE HERE
		settlements.add(newSettlement);
	}



	// STEPS 7-10: INSERT METHODS HERE, i.e. those similar to addSettlement and required
	// by the Application class
	
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
	public void save() throws IOException {
		// STEP 6: INSERT CODE HERE
		//PrintWriter outfile = new PrintWriter(new FileWriter(fileName));
	}

	public String toString() {
		String result = "";
		// INSERT CODE HERE
		return result;
	}
}
