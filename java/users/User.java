package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import flights.Itinerary;

/**
 * User class is an abstract class that deals with user data.
 * All user items require three data fields, which are first & last names, and
 * an email.
 * @author Lukas
 *
 */
public abstract class User implements Serializable
{
	private static final long serialVersionUID = 1720390190244612938L;

	//Instance Variables
	private String first;
	private String last;
	private String email;

	

	/**
	 * Constructs a User object that stores the user's last name, 
	 * first name, email,
	 * address, email, creditCardNumber and expiry date.
	 * 
	 * @param lastName			user's last name.
	 * @param firstNames		user's first names.
	 * @param email				user's email address.
	 * //@param address			user's home address.
	 * //@param creditCardNumber	user's credit card number.
	 * //@param expiryDate		user's credit card expiry date.
	 */
	public User(String lastName, String firstNames, String email)
	{
		this.last = lastName;
		this.first = firstNames;
		this.email = email;
	}
	
	@Override
	public String toString()
	{
		return this.last + "," + this.first + "," + this.email;
		
	}


	/**
	 * Gets First Name of user
	 * @return	first name of user
	 */
	public String getFirst() {
		return first;
	}


	/**
	 * Gets the last name of user
	 * @return	last name of user
	 */
	public String getLast() {
		return last;
	}


	/**
	 * gets the email of user
	 * @return	email address of user
	 */
	public String getEmail() {
		return email;
	}


	/**
	 *  sets user's first name
	 * @param first		user's first name
	 */
	public void setFirst(String first) {
		this.first = first;
	}

	/**
	 * sets user's last name
	 * @param last user's last name
	 */
	public void setLast(String last) {
		this.last = last;
	}

	/**
	 * sets user's email address
	 * @param email	user's email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
