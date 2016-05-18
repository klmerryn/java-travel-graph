package driver;



import com.example.kd.flightfinder.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import flights.Flight;
import flights.Itinerary;
import managers.AccountManager;
import managers.BookingManager;
import someExceptions.NoSuchNodeException;
import travelGraph.Edge;
import travelGraph.Graph;
import travelGraph.Node;
import users.Client;
import users.User;

/** A Driver used for autotesting the project backend. */
public class Driver {

    private static String userSavePath = "";
    private static String flightSavePath = "";
    /**
     * Uploads client information to the application from the file at the
     * given path.
     * @param path the path to an input csv file of client information with
     * lines in the format: 
     * LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
     *  (the ExpiryDate is stored in the format YYYY-MM-DD)
     */
    public static void uploadClientInfo(String path) {
        String[] givenPath = path.split("/");
        userSavePath = "";
        for (int i = 0; i< givenPath.length -1; i++)
        {
            userSavePath += givenPath[i] +"/";
        }
        userSavePath += "users.ser";
        File usersSaveFile = new File(userSavePath);
        AccountManager am = new AccountManager(usersSaveFile);
        am.uploadClientInfo(path);
    }     
    
    /**
     * Uploads flight information to the application from the file at the
     * given path.
     * @param path the path to an input csv file of flight information with 
     * lines in the format: 
     * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price,NumSeats
     * (the dates are in the format YYYY-MM-DD HH:MM; the price has exactly two
     * decimal places; the number of seats is a non-negative integer)
     */
    public static void uploadFlightInfo(String path) {
        String[] givenPath = path.split("/");
        flightSavePath = "";
        for (int i = 0; i< givenPath.length -1; i++)
        {
            flightSavePath += givenPath[i] +"/";
        }
        flightSavePath += "flights.ser";
        File flightSaveFile = new File(flightSavePath);
        BookingManager bm = new BookingManager(flightSaveFile);
        bm.uploadFlightInfo(path);
    }
    
    /**
     * Returns the information stored for the client with the given email. 
     * @param email the email address of a client
     * @return the information stored for the client with the given email
     * in this format:
     * LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
     * (the ExpiryDate is stored in the format YYYY-MM-DD)
     */
    public static String getClient(String email) {
        //TODO: File path for AccountManager
        File usersSaveFile = new File(userSavePath);
        AccountManager am = new AccountManager(usersSaveFile);
        Map<String, User> users= am.getUsers();
        if(users.containsKey(email))
        {
            Client client = (Client) users.get(email);
            return client.toString();
        }
        return null; //
    }

    /**
     * Returns all flights that depart from origin and arrive at destination on
     * the given date. 
     * @param date a departure date (in the format YYYY-MM-DD)
     * @param origin a flight origin
     * @param destination a flight destination
     * @return the flights that depart from origin and arrive at destination
     *  on the given date formatted with one flight per line in exactly this
     *  format:
     * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price
     * (the departure and arrival date and time are in the format
     * YYYY-MM-DD HH:MM; the price has exactly two decimal places) 
     */
    public static String getFlights(String date, String origin, String destination) {
        File flightSaveFile = new File(flightSavePath);
        BookingManager bm = new BookingManager(flightSaveFile);
        return bm.getFlights(date, origin, destination);
    }

    /**
     * Returns all itineraries that depart from origin and arrive at
     * destination on the given date. If an itinerary contains two consecutive
     * flights F1 and F2, then the destination of F1 should match the origin of
     * F2. To simplify our task, if there are more than 6 hours between the
     * arrival of F1 and the departure of F2, then we do not consider this
     * sequence for a possible itinerary (we judge that the stopover is too
     * long). In addition, the itineraries returned do not contain cycles, i.e., 
     * they do not visit the same place more than once.
     *
     * Every flight in an itinerary must have at least one seat
     * available for sale. That is, the itinerary must be bookable.
     *
     * @param date a departure date (in the format YYYY-MM-DD)
     * @param origin a flight original
     * @param destination a flight destination
     * @return itineraries that depart from origin and arrive at
     * destination on the given date with stopovers at or under 6 hours.
     * Each itinerary in the output should contain one line per flight,
     * in the format:
     * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
     * followed by total price (on its own line, exactly two decimal places)
     * followed by total duration (on its own line, in format HH:MM).
     */
    public static String getItineraries(String date, String origin, String destination) {
        File flightSaveFile = new File(flightSavePath);
        BookingManager bm = new BookingManager(flightSaveFile);
        ArrayList<ArrayList<Flight>> paths = bm.getFlightPaths(date, origin,
                destination);

        String itineraries = new String();

        for (ArrayList<Flight> flightPath : paths) {
            Itinerary i = new Itinerary(flightPath);
            itineraries += i;
            int hours = i.getTravelTime()/60;
            int minutes = i.getTravelTime()%60;
            String formattedMinutes = String.format("%02d", minutes);
            String formattedHours = String.format("%02d", hours);
            String formattedCost = String.format("%.2f", i.getTotalCost());
            itineraries += formattedCost + "\n" + formattedHours +":" + formattedMinutes + "\n";
        }

        return itineraries;
    }

    /**
     * Returns the same itineraries as getItineraries produces, but sorted according
     * to total itinerary cost, in non-decreasing order.
     * @param date a departure date (in the format YYYY-MM-DD)
     * @param origin a flight original
     * @param destination a flight destination
     * @return itineraries (sorted in non-decreasing order of total itinerary cost) 
     * that depart from origin and arrive at
     * destination on the given date with stopovers at or under 6 hours.
     * Each itinerary in the output should contain one line per flight,
     * in the format:
     * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
     * followed by total price (on its own line, exactly two decimal places)
     * followed by total duration (on its own line, in format HH:MM).
     */
    public static String getItinerariesSortedByCost(String date, String origin, String destination) {
        File flightSaveFile = new File(flightSavePath);
        BookingManager bm = new BookingManager(flightSaveFile);
        ArrayList<Itinerary> itineraries = bm.getItineraries(bm.getFlightPaths(date, origin, destination));
        itineraries = bm.sortItinerariesByCost(itineraries);
        String sortedItineraries = "";
        for(Itinerary itinerary: itineraries)
        {
            sortedItineraries += itinerary.allFlightInfo();
        }
        return sortedItineraries;
    }
    
    /**
     * Returns the same itineraries as getItineraries produces, but sorted according
     * to total itinerary travel time, in non-decreasing order.
     * @param date a departure date (in the format YYYY-MM-DD)
     * @param origin a flight original
     * @param destination a flight destination
     * @return itineraries (sorted in non-decreasing order of travel itinerary travel time) 
     * that depart from origin and arrive at
     * destination on the given date with stopovers at or under 6 hours.
     * Each itinerary in the output should contain one line per flight,
     * in the format:
     * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
     * followed by total price (on its own line, exactly two decimal places),
     * followed by total duration (on its own line, in format HH:MM).
     */
    public static String getItinerariesSortedByTime(String date, String origin, String destination) {
        File flightSaveFile = new File(flightSavePath);
        BookingManager bm = new BookingManager(flightSaveFile);
        ArrayList<Itinerary> itineraries = bm.getItineraries(bm.getFlightPaths(date, origin, destination));
        itineraries = bm.sortItinerariesByTime(itineraries);
        String sortedItineraries = "";
        for(Itinerary itinerary: itineraries)
        {
            sortedItineraries += itinerary.allFlightInfo();
        }
        return sortedItineraries;
    }
}
