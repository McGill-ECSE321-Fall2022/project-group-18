package Model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



// line 37 "model.ump"
// line 119 "model.ump"
public class Owner extends Person
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Owner Associations
    private Business business;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Owner(String aUsername, String aPassword, Business aBusiness)
    {
        super(aUsername, aPassword);
        if (aBusiness == null || aBusiness.getOwner() != null)
        {
            throw new RuntimeException("Unable to create Owner due to aBusiness. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
        business = aBusiness;
    }

    public Owner(String aUsername, String aPassword, int aTicketFeeForBusiness)
    {
        super(aUsername, aPassword);
        business = new Business(aTicketFeeForBusiness, this);
    }

    //------------------------
    // INTERFACE
    //------------------------
    /* Code from template association_GetOne */
    public Business getBusiness()
    {
        return business;
    }

    public void delete()
    {
        Business existingBusiness = business;
        business = null;
        if (existingBusiness != null)
        {
            existingBusiness.delete();
        }
        super.delete();
    }

}
