package users;

import java.io.Serializable;

/**
 * Admin class which extends the User class and is erializable.
 * This class deals with administrators' information.
 */
public class Admin extends User implements Serializable {

    public Admin(String lastName, String firstNames, String email)
    {
        super(lastName, firstNames, email);
    }

}
