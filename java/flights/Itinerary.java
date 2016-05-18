package flights;



import java.io.Serializable;
import java.util.ArrayList;


/**
 * An itinerary class which stores a list array of all the flights in
 * the itinerary. It also stores the values (which are calculated) of
 * total travel time and cost, along with the origin and the departure
 * date.
 * 
 * @author Anthony
 */

public class Itinerary implements Serializable, Travels { // need serial long uid thing
	
	//instance variables
	private double totalCost;
	private int travelTime;
	private ArrayList<Flight> flights;
	private String origin;
	private String departureDate;
	
	/**
	 * Creates a new itinerary object, using the flights list array.
	 * All other variables are calculated/found using said list.
	 * 
	 * @param flights	An array list of all the flights in the flight plan.
	 */
	
	public Itinerary(ArrayList<Flight> flights)
	{
		this.flights = flights;
		for(int x = 0; x < flights.size(); x++)
		{
			totalCost += flights.get(x).getCost();
		}
		travelTime = Flight.calculateTravelTime(flights.get(0)
				.getDepartureDate(), flights.get(flights.size()-1).
				getArrivalDate());
		origin = flights.get(0).getOrigin();
		departureDate = flights.get(0).getDepartureDate();
		
	}

	/**
	 * Returns the total cost of all flights in the itinerary.
	 * 
	 * @return A double value of the total cost
	 */
	public double getTotalCost() {
		return totalCost;
	}

	/**
	 * Returns the total travel time of all flights in the itinerary.
	 * 
	 * @return An integer value of the total travel time in minutes
	 */
	public int getTravelTime() {
		return travelTime;
	}

	
	/**
	 * Returns an array list of all the flights.
	 * 
	 * @return An array list of flights.
	 */
	public ArrayList<Flight> getFlights() {
		return flights;
	}

	
	/**
	 * Returns the origin of the itineraries, i.e. the starting point of the
	 * flight plan.
	 * 
	 * @return A string of the origin city. 
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Returns the departure date of the first flight in the itinerary.
	 * 
	 * @return A string of the departure date of the first flight.
	 */
	public String getDepartureDate() {
		return departureDate;
	}
	
	/**
	 * Returns all information except the cost of each of the flights in the 
	 * itinerary 
	 * 
	 * @return String of info. (everything except cost) about each flight 
	 * in itinerary
	 */
	@Override 
	public String toString() {
		String s = new String();
		for (Flight flight : flights) {
			s += flight.getFlightNoCost() + "\n";
		}
		return s;
	}

	/**
	 * Returns all information of the flights in the itinerary
	 *
	 *
	 * @return String of info. about each flight in itinerary
	 */
	public String allFlightInfo() {
		String s = new String();
		for (Flight flight : flights) {
			s += flight + "\n";
		}
		return s;
	}
}
