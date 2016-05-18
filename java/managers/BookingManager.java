package managers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import constants.Constants;
import someExceptions.NoSuchNodeException;
import someExceptions.NoSuchPathException;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


import flights.Flight;
import flights.Itinerary;
import travelGraph.Edge;
import travelGraph.Graph;
import travelGraph.Node;

/**
 * BookingManager will handle most of the work to do with booking.
 * It stores and creates complete flight directory of all flights in a graph,
 * initiates the search to generate a list of new itineraries given origin,
 * departure date inputs. Furthermore, it sorts itineraries by order of cost
 * and travel time. 
 * 
 * @author Anthony 
 *
 */

public class BookingManager implements Serializable{

	//Graph is used to store flight numbers and flight objects
	private Graph<String, Flight> flightDirectory;
	//Initiates a logger
	private static final Logger logger =
	            Logger.getLogger(BookingManager.class.getName());
	private static final Handler consoleHandler = new ConsoleHandler();
	//save path
	private String savePath;

	/**
	 * Create a new bookingManager with an empty flightDirectory.
	 */
	public BookingManager() {
		this.flightDirectory = new Graph<String, Flight>();
	}


	/**
	 * Creates a new bookingManager with a flightDirectory containing the
	 * information stored in filePath.
	 * 
	 * @param file the file path used to populate flightDirectory
	 */
	public BookingManager(File file)
	{
		// Associate the handler with the logger.
        logger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        
		this.flightDirectory = new Graph<String, Flight>();
		this.savePath = file.getPath();

		try
		{
			// Reads serializable objects from file.
			// Populates users Map using stored data, if it exists.
			//File file = new File(filePath);
			if (file.exists()) {
				readFromFile(file.getPath());
			} else {
				file.createNewFile();
			}
		}
		catch (IOException e)
		{
			logger.log(Level.SEVERE, "Could not find file to save to.", e);
		}

		this.savePath = file.getPath();
		
	}

	public String uploadFlightInfo(String filePath)
	{
		String newlyUploaded = "Upload Failed";
		try
		{
			// new Scanner object
			Scanner sc = new Scanner(new File(filePath));
			//Will be used to read in each line from the file
			String line;
			//Resets newlyUploaded since scanner has been initiated
			newlyUploaded = "Newly Registered Flights:\n";

			while (sc.hasNextLine()) //while there is another line in the file
			{
				//gets the next line in file
				line = sc.nextLine();
				//Splits the String at each "," and creates a String Array
				String[] entries = line.split(",");
				//Creates a new Flight object with the entries
				Flight flight = new Flight(entries[0], entries[1], entries[2], entries[3],
						entries[4], entries[5], Double.parseDouble(entries[6]),
						Integer.parseInt(entries[7]));
				//add the flight to the list of flights
				this.addFlight(flight);

				newlyUploaded += flight.toString()+"\n";
			}
			//saves the added flights to the file
			this.saveToFile(this.savePath);
			sc.close();
		}
		catch(FileNotFoundException e)
		{
			logger.log(Level.SEVERE, "Could not find csv file at"+filePath, e);
		}
		return newlyUploaded;
	}
	
	/**
	 * Reads the file from the given path.
	 * Populates the flightDirectory graph if the file exists
	 * @param path	String path to the file to be read from
	 */
	public void readFromFile(String path)
	{
		try 
		{
			InputStream file = new FileInputStream(path);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			
			//deserialize the Graph
			flightDirectory = (Graph<String, Flight>) input.readObject();
			input.close();
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "Cannot Read from input.", e);
		}
	}
	
	/**
	 * Saves the flightDirectory Graph to the file given by filePath
	 * 
	 * @param filePath	String filePath to the file to save to.
	 */
	public void saveToFile(String filePath)
	{
		try
		{
			OutputStream file = new FileOutputStream(filePath);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);

			// serialize the Map
			output.writeObject(flightDirectory);
			output.close();
		}
		catch (IOException e)
		{
			//Logs if an error occurred
			logger.log(Level.SEVERE, "Cannot save to File.", e);
		}
	}


	/**
	 * Finds the flights going straight from an origin to a destination on a
	 * given date.
	 *
	 * The origin and destination are passed as strings.
	 *
	 * @param date			String of the departure date
	 * @param origin		String of the origin city
	 * @param destination	String of the destination city
	 * @param result		ArrayList<Flight> of all flights going straight
	 *                      from origin to destination on the provided date.
	 */
	public void findFlights(String date, String origin, String destination,
							ArrayList<Flight> result)
	{
		try {
			Node<String> originNode = flightDirectory.getNode(origin);
			Node<String> destNode = flightDirectory.getNode(destination);
			result = getFlights(originNode, destNode, date);
		} catch (NoSuchNodeException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gets an arrayList of flights for all those flights going directly from
	 * the origin to the destination on a certain date.
	 *
	 * The origin and destination are passed as nodes rather than strings.
	 *
	 * @param origin		A node of the origin city
	 * @param destination	A node of the destination city
	 * @param date			The date of departure
	 * @return				An arrayList of all the flights going directly
	 * 						from destination to origin.
	 */
	private ArrayList<Flight> getFlights(Node<String> origin,
										 Node<String> destination,
										 String date) {
		ArrayList<Flight> result = new ArrayList<Flight>();

		if (origin.equals(destination)) {
			return result;
		}
		else {
			Set<Edge<Flight, String>> allFlights =
					flightDirectory.getEdges(origin);
			for (Edge<Flight, String> flight : allFlights) {
				Node<String> destNode = flight.getPointingTo();
				String depDate = flight.getValue().getDepartureDateOnly();
				if (destNode.equals(destination) && depDate.equals(date)) {
					result.add(flight.getValue());
				}
			}
			return result;
		}
	}

	/**
	 * Returns flight directory flightDirectory.
	 * 
	 * @return Graph<String, Flight> flight directory
	 */
	public Graph<String, Flight> getFlightDirectory() {
		return flightDirectory;
	}


	/**
	 * Adds a flight to flight directory flightDirectory.
	 * 
	 * @param flight object to be added
	 */
	public void addFlight(Flight flight) {
		String origin = flight.getOrigin();
	    String destination = flight.getDestination();
	    
	    //Checks if the origin and destination city are already nodes in 
	    //  the graph
	    if(!flightDirectory.containsNode(origin)) {
	    	flightDirectory.addNode(origin);
	    }
	    if(!flightDirectory.containsNode(destination)) {
	        flightDirectory.addNode(destination);
	    }
	    Node<String> pointingFrom;
	    Node<String> pointingTo;
		Node<String> newPointingFrom;
		Node<String> newPointingTo;
	    try {
	    	
	    	//Attempts to get the origin and destination nodes and make an edge
	    	pointingFrom = flightDirectory.getNode(origin);
			newPointingFrom = flightDirectory.getNode(origin);
	        pointingTo = flightDirectory.getNode(destination);
			newPointingTo = flightDirectory.getNode(destination);

			Collection<Set<Edge<Flight, String>>> allEdges =
					flightDirectory.getContents().values();

			// If flightDirectory already contains a flight with the same
			// flight number as the one about about be added, reset the
			// value of the edge instead.
			for (Set<Edge<Flight, String>> setEdges : allEdges) {
				for (Edge<Flight, String> e : setEdges) {
					String ExistingFlightNum = e.getValue().getFlightNumber();
					String newFlightNum = flight.getFlightNumber();
					if (newFlightNum.equals(ExistingFlightNum)) {
						Edge<Flight, String> oldEdge =
								new Edge<Flight, String>(e.getValue());
						e.setValue(flight);

						// If origin or destination of this flight have
						// changed, alter mapping accordingly.
						String oldOrigin = e.getPointingFrom().getValue();
						String newOrigin = flight.getOrigin();
						if (!oldOrigin.equals(newOrigin)) {
							if (!flightDirectory.containsNode(newOrigin)) {
								flightDirectory.addNode(newOrigin);
							}
							newPointingFrom = flightDirectory.getNode(newOrigin);
						}

						String oldDest = e.getPointingTo().getValue();
						String newDest = flight.getDestination();
						if (!oldDest.equals(newDest)) {
							if (!flightDirectory.containsNode(newDest)) {
								flightDirectory.addNode(newDest);
							}
							newPointingTo = flightDirectory.getNode(newDest);
						}

						// Remove edge from current set and set new key for
						// this edge if pointingTo has changed.
						flightDirectory.addEdge(newPointingFrom, newPointingTo,
								e);
						if (!newPointingFrom.equals(pointingFrom)) {
							Set<Edge<Flight, String>> edges =
									flightDirectory.getEdges(pointingFrom);
							if (edges.size() == 1) {
								flightDirectory.getContents().
										remove(pointingFrom);
							}
							else {
								edges.remove(oldEdge);
							}
						}
						return;
					}
				}
			}


	        Edge<Flight, String> edge = new Edge<Flight, String>(flight,
	        		pointingFrom, pointingTo);

			//Adds the edge to the graph
			flightDirectory.addEdge(pointingFrom, pointingTo, edge);
		} catch (NoSuchNodeException e) {
				
			e.printStackTrace();
		}   
	}

	
	/**
	 * Sorts the list of itineraries itineraries in ascending order 
	 * based on total cost. If the list is empty, returns null.
	 * 
	 * @param itineraries An arrayList of itineraries to be sorted.
	 * @return A sorted list of itineraries in ascending order of cost
	 */
	public ArrayList<Itinerary> sortItinerariesByCost(ArrayList<Itinerary>
											itineraries)
	{
		if(itineraries.size() == 0)
			return null;
		if(itineraries.size() == 1)
			return itineraries;
		int x;
		Itinerary currItin;
		int y;
		
		for (x = 1; x < itineraries.size(); x++)    
		{
			currItin = itineraries.get(x);
	    	for(y = x - 1; (y >= 0) && (itineraries.get(y).getTotalCost() 
	    			> currItin.getTotalCost()); y--)
	    	{
	    		itineraries.set(y+1, itineraries.get(y));
	    	}
	    	itineraries.set(y+1, currItin);
	    }
		return itineraries;
	}
	

	
	/**
	 * Sorts a list of itineraries in ascending order based on total
	 * travel time. If the list is empty, returns null.
	 * 
	 * @param itineraries A list of itinerary objects to be sorted.
	 * @return A list of itineraries sorted in ascending order of travel time.
	 */
	public ArrayList<Itinerary> sortItinerariesByTime
						(ArrayList<Itinerary> itineraries)
	{
		if(itineraries.size() == 0)
			return null;
		if(itineraries.size() == 1)
			return itineraries;
		int x;
		Itinerary currItin;
		int y;
		
		for (x = 1; x < itineraries.size(); x++)    
		{
			currItin = itineraries.get(x);
	    	for(y = x - 1; (y >= 0) && (itineraries.get(y).getTravelTime() 
	    			> currItin.getTravelTime()); y--)
	    	{
	    		itineraries.set(y+1, itineraries.get(y));
	    	}
	    	itineraries.set(y+1, currItin);
	    }
		return itineraries;
	}


	/**
	 * Wrapper method for generateFlightPaths. Takes String input and generates
	 * flight paths based on interpretation of Strings as Node (city) and time
	 * (date) parameters.
	 *
	 * It returns ArrayList<Itinerary> of all itineraries that match the
	 * parameter requirements.
	 *
	 * @param origin		Departure city of first flight
	 * @param destination	Arrival city of last flight
	 * @param date			Departure date of first flight
	 * @return				ArrayList of itineraries for all itineraries that
	 * 						match the parameters.
	 */
    public ArrayList<Itinerary> getTrips(String origin, String destination, String date){
        Node<String> originNode;
        Node<String> destinationNode;
        ArrayList<Itinerary> tripItineraries = new ArrayList<>();

        ArrayList<ArrayList<Flight>> trips = new ArrayList<>();
        ArrayList<Flight> path = new ArrayList<>();
        ArrayList<Node<String>> visited = new ArrayList<>();

        try {
            originNode = this.flightDirectory.getNode(origin);
            destinationNode = this.flightDirectory.getNode(destination);
            generateFlightPaths(originNode, destinationNode, path, visited,
					date, trips, Constants.MAX_STOP_OVER);
        }
        catch (NoSuchNodeException | NoSuchPathException e) {
            System.out.println("Invalid input: \n" + e.getStackTrace());
        }

        // Convert the ArrayList<ArrayList<Flight>> into ArrayList<Itinerary>
        tripItineraries = getItineraries(trips);
        return tripItineraries;
    }


	/**
	 * Convert a nested ArrayList of Flight objects to a flat list of
	 * Itinerary objects.
	 *
	 * @param paths	The nested arraylist of flights (nested arrayList
	 *              represents a single itinerary)
	 * @return		An arrayList of itineraries.
	 */
    public ArrayList<Itinerary> getItineraries(ArrayList<ArrayList<Flight>> paths) {
        ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();

        for (ArrayList<Flight> flightPath : paths) {
            Itinerary i = new Itinerary(flightPath);
            itineraries.add(i);
        }
        return itineraries;
    }


	/**
	 * Searches Graph for all possible flights from given Origin to 
	 * given Destination on appropriate date and time. Returns an
	 * ArrayList of Flight objects representing a valid ordered Path from 
	 * origin to destination (the last element being the Flight object that
	 * ends at the final destination.
	 * 
	 * If it is not possible to reach destination from origin, throws
	 * NoSuchPathException.
	 * 
	 * @param origin		Starting Node for search
	 * @param dest			Desired end node
	 * @param path			Array of valid Flights to reach destination node
	 * @param visited		Nodes visited (no repeat visits permitted in one 
	 * 						path)
	 * @param date			Date on which flight leaving origin must leave
	 * @param result 		The final list of all possible flight paths
	 * @param maxStopOver	The maximum length of a stopover in minutes.
	 * 
	 * @throws NoSuchPathException if no such path is found.
	 * @throw NoSuchNodeException if given node is not found
	 */
	
	public void generateFlightPaths(Node<String> origin, Node<String> dest,
			ArrayList<Flight> path, ArrayList<Node<String>> visited, 
			String date, ArrayList<ArrayList<Flight>> result, int maxStopOver)
					throws NoSuchPathException, NoSuchNodeException {
		
		// Base case: check whether already reached destination
		if (origin.equals(dest)) {
			result.add(path); // Path is complete  
		}
		else {
			ArrayList<Node<String>> newVisited = 
					new ArrayList<Node<String>>(visited);
			newVisited.add(origin);
			
			// Examine all Edges leaving from current Node
			for (Edge<Flight, String> edge : 
				flightDirectory.getEdges(origin)) {
				
				// If the node to which this edge connects has not been
				// visited yet, check the validity of the flight it stores.
				if (!newVisited.contains(edge.getPointingTo())) {
					boolean isValidEdge = false;
					
					// Check that this flight leaves on date requested and has
					// space remaining.
					if (newVisited.size() == 1) {
						String flightDepDate
								= edge.getValue().getDepartureDateOnly();
						int numSeats = edge.getValue().getNumSeats();
						isValidEdge = flightDepDate.equals(date)
								&& numSeats > 0;
					}
					
					// Check that this flight leaves within six hours of the
					// last stopover and has space remaining.
					else {
						Flight prevFlight = path.get(path.size() - 1);
						Flight curFlight = edge.getValue();
						int numSeats = curFlight.getNumSeats();
						int lenStopOver = 
								Flight.calculateTravelTime(
										prevFlight.getArrivalDate(), 
										curFlight.getDepartureDate());
						
						isValidEdge = (lenStopOver <= maxStopOver && 
								lenStopOver >= 0) && numSeats > 0;
					}
					
					if (isValidEdge) {
						ArrayList<Flight> newPath = 
								new ArrayList<Flight>(path);
						newPath.add(edge.getValue());
						Node<String> newOrigin = edge.getPointingTo();
						generateFlightPaths(newOrigin, dest, newPath, 
								newVisited, date, result, maxStopOver);
						newPath = null;
					}
				}
			}
		}
	}


	/**
	 * Returns a string representation of all the flights straight from the
	 * origin to the destination on a certain date.
	 * The string is of the format,  flightNumber, departureDate,arrivalDate,
	 * airline, origin. destination, cost.
	 *
	 * @param date			Departure date of the flight
	 * @param origin		Departure city of the flight
	 * @param destination	Arrival city of the flight
	 * @return				Flight information without numSeats and travelTime
	 */
	public String getFlights(String date, String origin, String destination)
	{
		String result = new String();
		try{
			Node<String> originNode = flightDirectory.getNode(origin);
			for (Edge<Flight, String> edge
					: flightDirectory.getEdges(originNode)) {
				Flight flight = edge.getValue();
				if (flight.getDestination().equals(destination)
						&& flight.getDepartureDateOnly().equals(date)) {
					result += flight.getFlightNoNumSeats() + "\n";
				}
			}
		}
		catch (NoSuchNodeException e) {
			System.out.println("Sorry, invalid Node request: ");
			e.printStackTrace();
		}
		return result;
	}



	/**
	 * Returns all sequences of flights that get from origin to destination on
	 * date date.
	 * @param date The date on which the flight from origin must leave.
	 * @param origin The place from which the first flight in the sequence
	 * must originate.
	 * @param destination The destination of the last flight flight
	 * in the sequence.
	 * @return All sequences of flights that get from origin to destination on
	 * date date.
	 */
	public ArrayList<ArrayList<Flight>> getFlightPaths(String date,
													   String origin,
													   String destination) {

		ArrayList<ArrayList<Flight>> allFlightPaths =
				new ArrayList<ArrayList<Flight>>();
		ArrayList<Flight> path = new ArrayList<Flight>();
		ArrayList<Node<String>> visited = new ArrayList<Node<String>>();

		// Get nodes based on search parameters. If invalid Nodes, skips them.
		try {
			Node<String> originNode = flightDirectory.getNode(origin);
			Node<String> destNode = flightDirectory.getNode(destination);

			// Generate Flight Paths. If there are no possible flight paths
			// from origin to destination, print an error message and
			// the stack Trace.
			try {
				generateFlightPaths(originNode, destNode, path,
						visited, date, allFlightPaths, Constants.MAX_STOP_OVER);
			}
			catch (NoSuchPathException exception) {
				System.out.println("Invalid path: ");
				exception.printStackTrace();
			}
		}
		catch (NoSuchNodeException exception) {

		} // Ignores requests for invalid nodes.

		return allFlightPaths;
	}




}
