import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Represents a settlement with a name, population, type and
 * with connected roads.
 *
 * @author Chris Loftus
 * @version 3.0 (14th March 2015)
 */
public class Road {
    private String name;
    private Classification classification;
    private Settlement sourceSettlement;
    private Settlement destinationSettlement;
    private double length;

    /**
     * Constructor to build road between two settlements. This fulfills the class diagram
     * constraint that every road must be connected to two settlements. We are not
     * checking whether it's the same settlement, but that's okay: you drive out and arrive
     * back again at the same place!
     *
     * @param nm          The road name
     * @param classifier  The class of road, e.g. 'A'
     * @param source      The source settlement
     * @param destination The destination settlement (can be the same as the source!)
     */
    public Road(String nm,
                Classification classifier,
                Settlement source,
                Settlement destination,
                double len) {

        // This is from worksheet 8. We now also have a road length
        name = nm;
        classification = classifier;
        sourceSettlement = source;
        source.add(this);
        destinationSettlement = destination;
        destination.add(this);
        length = len;
    }

    /**
     * The name of the road
     *
     * @return The road's name
     */
    public String getName() {
        return name;
    }

    public void setName(String nm) {
        name = nm;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double len) {
        length = len;
    }

    /**
     * The road's class
     *
     * @return The class of the road, e.g. A
     */
    public Classification getClassification() {
        return classification;
    }

    /**
     * The source settlement
     *
     * @return One end of the road we call source
     */
    public Settlement getSourceSettlement() {
        return sourceSettlement;
    }

    /**
     * The destination settlement
     *
     * @return One end of the road we call destination
     */
    public Settlement getDestinationSettlement() {
        return destinationSettlement;
    }

    /**
     * Provided a settlement, return the other settlement connected to the road.
     * If the passed settlement isn't connected to the road, return null.
     *
     * @param s The Settlement to cross check.
     * @return The other settlement connected to the road.
     */
    public Settlement getAlternateSettlement(Settlement s) {
        boolean source = sourceSettlement.equals(s);
        boolean dest = destinationSettlement.equals(s);

        if (!(source || dest)) {
            return null;
        }
        if (source) {
            return destinationSettlement;
        } else if (dest) {
            return sourceSettlement;
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Road other = (Road) obj;
        if (destinationSettlement == null) {
            if (other.destinationSettlement != null)
                return false;
        } else if (!destinationSettlement.equals(other.destinationSettlement))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sourceSettlement == null) {
            if (other.sourceSettlement != null)
                return false;
        } else if (!sourceSettlement.equals(other.sourceSettlement))
            return false;
        return true;
    }


    /**
     * Some useful info about the state of this object. Notice how this also
     * returns the names of the connected settlements
     *
     * @return The state of the object
     */
    public String toString() {
        String result = "";
        result += "Road Name=" + this.getName() +
                ", Classification=" + this.getClassification().toString() +
                ", length=" + this.getLength() +
                ", Source Settlement=" + this.getSourceSettlement().getName() +
                ", Destination Settlement=" + this.getDestinationSettlement().getName() + "\n";
        return result;
    }
}
