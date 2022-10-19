package Model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



/**
 * class DonatedArtifact{
 * -- 1 Customer;
 * 1 -- 1 Artifact;
 * }
 */
// line 31 "model.ump"
// line 114 "model.ump"
public abstract class Person
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Person Attributes
    private String username;
    private String password;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Person(String aUsername, String aPassword)
    {
        username = aUsername;
        password = aPassword;
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setUsername(String aUsername)
    {
        boolean wasSet = false;
        username = aUsername;
        wasSet = true;
        return wasSet;
    }

    public boolean setPassword(String aPassword)
    {
        boolean wasSet = false;
        password = aPassword;
        wasSet = true;
        return wasSet;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public void delete()
    {}


    public String toString()
    {
        return super.toString() + "["+
                "username" + ":" + getUsername()+ "," +
                "password" + ":" + getPassword()+ "]";
    }
}
