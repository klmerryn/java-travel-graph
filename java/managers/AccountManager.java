package managers;

import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import users.Client;
import users.User;

/**
 * AccountManager manages all the user relevant data. It stores a hashMap of
 * all the user account. It can add new records to the users map by reading
 * from a CSV file. It is a serializable class that must be instantiated with
 * an address of the .ser file.
 * map.
 * @author Lukas
 *
 */
public class AccountManager implements Serializable
{
	private static final long serialVersionUID = 7675640778884723444L;
	
	private static final Logger logger =
            Logger.getLogger(AccountManager.class.getName());
    private static final Handler consoleHandler = new ConsoleHandler();
    
	//Map of users
	private Map<String, User> users;
	
	private String savePath;
	
	
	
	public AccountManager(File savePath)
	{
		this.users = new HashMap<String, User>();
		
		// Associate the handler with the logger.
        logger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

		try{
			// Reads serializable objects from file.
			// Populates users Map using stored data, if it exists.
			if (savePath.exists())
			{
				readFromFile(savePath.getPath());
			} else
			{
				savePath.createNewFile();
			}
		}
		catch (IOException e)
		{
			//Logs if an error occurred
			logger.log(Level.SEVERE, "Could not find save File", e);
		}

        
        this.savePath = savePath.getPath();
	}
	
	/**
	 * Adds a User to the users List
	 * @param user	User to be added to the users list
	 */
	public void addUser(User user)
	{
		//Adds the user to users list
		this.users.put(user.getEmail(), user);
		//Logs the user was added
		logger.log(Level.FINE, "Added a new User" + user.getLast() + "," +
					user.getFirst());
	}
	
	/**
	 * Reads from file given by path. Populates the users List if the path 
	 * exists and is in the correct format.
	 * @param path	String path to the file to be read from
	 */
	public void readFromFile(String path)  
	{
		try
		{
			InputStream file = new FileInputStream(path);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			
			//deserialize the Map
			users = (Map<String, User>) input.readObject();
			input.close();
		}
		catch (Exception e)
		{
			//logs if an error occurred
			logger.log(Level.SEVERE, "Cannot read from input.", e);
		}
			

	}
	
	/**
	 * Saves the users list to the file given by filePath
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
			output.writeObject(users);
			output.close();
		}
		catch (IOException e)
		{
			//Logs if an error occurred
			logger.log(Level.SEVERE, "Cannot save to File.", e);
		}
	}

	/**
	 * Returns list of all users.
	 * @return users
	 */
	public Map<String, User> getUsers() 
	{
		return users;
	}

	/**
	 * Adds the users in the csv file found at path to the users Map
	 * @param path absolute path to a csv file to add clients to users
	 */
	public void uploadClientInfo(String path)  {
		
		//new Scanner object
		Scanner sc;
		try {
			sc = new Scanner(new File(path));
			//Will be used to read each line from the file in
			String line;
			
			while (sc.hasNextLine())//while there are still lines in the file
			{
				//gets the next line
				line = sc.nextLine();
				//Splits the line at each "," and creates a String Array
				String[] entries = line.split(",");
				//Creates new Client with "Last", "First", "email", "Address",
				// "credit card", "expiry"
				Client client = new Client(entries[0], entries[1], entries[2],
						entries[3], entries[4], entries[5]);
				this.addUser(client);

			}
			//save changes to the serialize file
			saveToFile(savePath);
			sc.close();
		} catch (FileNotFoundException e) {
			//Logs if an error occurred
			logger.log(Level.SEVERE, "Cannot save to File.", e);
		}
	}

	/**
	 * Returns a string of the emails, first and last names of all the users
	 * in the user map. The format is lastName, firstName, email.
	 * @return
	 */
	@Override
	public String toString()
	{
		String result = "";
		for (User u : users.values()) {
			result += u.toString() + "\n";
		}
		return result;
	}

}
