package flights;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;


/**
 * Flight object stores information on a flight. Stored info includes flightNumber, departure date
 * and time, arrival date and time, airline, origin, destination, cost, travel time,
 * and number of seats available.
 *
 * @author Lukas
 *
 */
public class Flight implements Serializable, Travels {
    //generated number
    private static final long serialVersionUID = -5054790685273759253L;

    //instance variables
    private String flightNumber;
    private String departureDate;
    private String arrivalDate;
    private String airline;
    private String origin;
    private String destination;
    private double cost;
    private int travelTime; //Travel time measured in minutes
    private int numSeats;


    /**
     * Constructs a Flight object that stores flightNumber, departure date
     * and time, arrival date and time, airline, origin, destination, cost,
     * travel time, and number of available seats
     *
     * @param flightNumber	Flight's flight number.
     * @param departure		Flight's departure date and time
     * @param arrival		Flight's arrival date and time
     * @param airline		Flight's airline
     * @param origin		Flight's origin
     * @param destination	Flight's destination
     * @param cost			Flight's cost
     * @param numSeats      Flight's number of available seats
     *
     */
    public Flight(String flightNumber, String departure, String arrival,
                  String airline, String origin, String destination,
                  double cost, int numSeats)
    {
        this.flightNumber = flightNumber;
        this.departureDate = departure;
        this.arrivalDate = arrival;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.cost = cost;
        //calculate the travel time of this flight based on the departure and
        // arrival
        this.travelTime = this.calculateTravelTime(departure, arrival);
        this.numSeats = numSeats;
    }

    /**
     * Calculates the travel time of the flight based on the departure date
     * and time and arrival date and time. Expected format of date and time
     * is "YYYY-MM-DD HH:mm".
     *
     * @param departureDateAndTime	Flight's departure date and time
     * @param arrivalDateAndTime	Flight's arrival date and time
     * @return	minutes of the difference between departureDateAndTime and
     * arrivalDateAndTime
     *
     */

    public static int calculateTravelTime(String departureDateAndTime,
                                          String arrivalDateAndTime)
    {

        //Creates a format object for the Date and time
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();


        //Creates Blank Date objects
        Date formatDepartureDate = null;
        Date formatArrivalDate = null;

        try
        {
            //attempts to get departure and arrival dates
            formatDepartureDate = format.parse(departureDateAndTime);
            formatArrivalDate = format.parse(arrivalDateAndTime);
            calendar1.setTime(formatDepartureDate);
            calendar2.setTime(formatArrivalDate);
        }
        catch (Exception e)
        {
            //if format is wrong
            System.out.println("Invalid Date Format");
        }

        //gets the time in miliseconds
        long cal1Time = calendar1.getTimeInMillis();
        long cal2Time = calendar2.getTimeInMillis();

        //Finds the difference in arrival and departure in milliseconds
        long difference = cal2Time - cal1Time;
        //gets the difference in minutes
        long diffMinutes = difference / 60000;

        return (int) diffMinutes; //converts to int before returning
    }


    /**
     * get Flight's flight number
     * @return	Flight's flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * get Flight's departure date and time
     * @return	Flight's departure date and time
     */
    public String getDepartureDate() {
        return departureDate;
    }

    /**
     * Gets the departure date of a flight only (no time)
     * @return	Departure Date of the flight
     */
    public String getDepartureDateOnly()
    {
        String departureDateOnly =  this.departureDate.substring(0, 10);
        return departureDateOnly;
    }

    /**
     * get Flight's arrival date and time
     * @return	Flight's arrival date and time
     */
    public String getArrivalDate() {
        return arrivalDate;
    }

    /**
     *  get Flight's airline
     * @return Flight's airline
     */
    public String getAirline() {
        return airline;
    }

    /**
     * get Flight's origin city
     * @return	Flight's origin city
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * get Flight's destination
     * @return Flight's destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * get Flight's cost
     * @return	Flight's cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * get Flight's travel time
     * @return	Flight's travel time
     */
    public int getTravelTime() {
        return travelTime;
    }


    /**
     * @param flightNumber the flightNumber to set
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @param arrivalDate the arrivalDate to set
     */
    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    /**
     * @param airline the airline to set
     */
    public void setAirline(String airline) {
        this.airline = airline;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * @param travelTime the travelTime to set
     */
    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    /**
     * Sets the number of seats remaining on this flight to numSeats.
     * @param numSeats the number of seats remaining on this flight
     */
    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    /**
     * Reduces the number of seats remaining on this fight by incrememt.
     * @param increment
     */
    public void incrementNumSeats(int increment) {
        this.numSeats -= increment;
    }

    /**
     * Returns the number of seats remaining on this flight.
     * @return the number of seats remaining on this flight.
     */
    public int getNumSeats() {
        return this.numSeats;
    }

    /**
     * Returns a string of all info. about the flight. The format is
     * flightNumber,departureDate, arrivalDate, airline, origin,
     * destination, cost, numSeats.
     * @return a string representation of the Flight object
     */
    @Override
    public String toString()
    {
        return this.flightNumber + ","+this.departureDate+","
                +this.arrivalDate+","+this.airline+","+this.origin+","+this.destination+","
                +String.format("%.2f", this.cost)+this.numSeats;
    }

    /**
     * Returns a String representation of the Flight
     * without the number of seats variable
     * @return	The Flight without the numSeats variable
     */
    public String getFlightNoNumSeats()
    {
        return this.flightNumber + ","+this.departureDate+","
                +this.arrivalDate+","+this.airline+","+this.origin+","+this.destination+","
                +String.format("%.2f", this.cost);
    }

    /**
     * Returns a String representation of the Flight
     * without the cost variable
     * @return	The Flight without the cost variable
     */
    public String getFlightNoCost()
    {
        return this.flightNumber + ","+this.departureDate+","
                +this.arrivalDate+","+this.airline+","+this.origin+","
                +this.destination;
    }

    /**
     * Overrides hashCode method.
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((departureDate == null) ? 0 : departureDate.hashCode());
        result = prime * result
                + ((destination == null) ? 0 : destination.hashCode());
        result = prime * result
                + ((flightNumber == null) ? 0 : flightNumber.hashCode());
        return result;
    }

    /**
     * Checks Flight against other object to determine equality
     * (based on flight ID, departure date/time, and destination).
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Flight))
            return false;
        Flight other = (Flight) obj;
        if (departureDate == null) {
            if (other.departureDate != null)
                return false;
        } else if (!departureDate.equals(other.departureDate))
            return false;
        if (destination == null) {
            if (other.destination != null)
                return false;
        } else if (!destination.equals(other.destination))
            return false;
        if (flightNumber == null) {
            if (other.flightNumber != null)
                return false;
        } else if (!flightNumber.equals(other.flightNumber))
            return false;
        return true;
    }
}
