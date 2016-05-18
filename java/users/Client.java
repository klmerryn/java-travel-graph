package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import flights.Itinerary;

/**
 * A client.
 */
public class Client extends User implements Serializable {

    private String address;
    private String creditCardNumber; //String: number is not manipulated
    private String expiryDate;
    private List bookedItineraries;

    public Client(String lastName, String firstNames, String email,
                String address, String creditCardNumber, String expiryDate) {

        super(lastName, firstNames, email);
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
        this.bookedItineraries = new ArrayList();
    }

    @Override
    public String toString()
    {
        return super.toString() +","+this.address+","+this.creditCardNumber+","+this.expiryDate;
    }


    /**
     * Add an booked Itinerary to the User
     * @param itin	Itinerary to add to bookedItineraries
     */
    public void addItinerary(Itinerary itin)
    {
        this.bookedItineraries.add(itin);
    }

    /**
     * get's the user's booked Itineraries
     * @return the bookedItineraries
     */
    public List getBookedItineraries() {
        return bookedItineraries;
    }

    /**
     * get address of user
     * @return	address of user
     */
    public String getAddress() {
        return address;
    }

    /**
     * get credit card number of user
     * @return	credit card number of user
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }


    /**
     * get the expiry date of the user's credit card
     * @return	expiry date of the user's credit card
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * sets user's home address
     * @param address	user's home address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * sets user's credit card number
     * @param creditCardNumber	user's credit card number
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * sets user's credit card expiry date. In the format "YYYY-MM-DD".
     * @param expiryDate	user's credit card expiry date
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

}
