/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 65 "model.ump"
// line 146 "model.ump"
public class Business
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Business Attributes
  private int ticketFee;

  //Business Associations
  private Owner owner;
  private List<BusinessHour> businessHours;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Business(int aTicketFee, Owner aOwner)
  {
    ticketFee = aTicketFee;
    if (aOwner == null || aOwner.getBusiness() != null)
    {
      throw new RuntimeException("Unable to create Business due to aOwner. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    owner = aOwner;
    businessHours = new ArrayList<BusinessHour>();
  }

  public Business(int aTicketFee, String aUsernameForOwner, String aPasswordForOwner)
  {
    ticketFee = aTicketFee;
    owner = new Owner(aUsernameForOwner, aPasswordForOwner, this);
    businessHours = new ArrayList<BusinessHour>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTicketFee(int aTicketFee)
  {
    boolean wasSet = false;
    ticketFee = aTicketFee;
    wasSet = true;
    return wasSet;
  }

  public int getTicketFee()
  {
    return ticketFee;
  }
  /* Code from template association_GetOne */
  public Owner getOwner()
  {
    return owner;
  }
  /* Code from template association_GetMany */
  public BusinessHour getBusinessHour(int index)
  {
    BusinessHour aBusinessHour = businessHours.get(index);
    return aBusinessHour;
  }

  public List<BusinessHour> getBusinessHours()
  {
    List<BusinessHour> newBusinessHours = Collections.unmodifiableList(businessHours);
    return newBusinessHours;
  }

  public int numberOfBusinessHours()
  {
    int number = businessHours.size();
    return number;
  }

  public boolean hasBusinessHours()
  {
    boolean has = businessHours.size() > 0;
    return has;
  }

  public int indexOfBusinessHour(BusinessHour aBusinessHour)
  {
    int index = businessHours.indexOf(aBusinessHour);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBusinessHours()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public BusinessHour addBusinessHour(Date aDay, Time aOpenTime, Time aCloseTime)
  {
    return new BusinessHour(aDay, aOpenTime, aCloseTime, this);
  }

  public boolean addBusinessHour(BusinessHour aBusinessHour)
  {
    boolean wasAdded = false;
    if (businessHours.contains(aBusinessHour)) { return false; }
    Business existingBusiness = aBusinessHour.getBusiness();
    boolean isNewBusiness = existingBusiness != null && !this.equals(existingBusiness);
    if (isNewBusiness)
    {
      aBusinessHour.setBusiness(this);
    }
    else
    {
      businessHours.add(aBusinessHour);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBusinessHour(BusinessHour aBusinessHour)
  {
    boolean wasRemoved = false;
    //Unable to remove aBusinessHour, as it must always have a business
    if (!this.equals(aBusinessHour.getBusiness()))
    {
      businessHours.remove(aBusinessHour);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBusinessHourAt(BusinessHour aBusinessHour, int index)
  {  
    boolean wasAdded = false;
    if(addBusinessHour(aBusinessHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBusinessHours()) { index = numberOfBusinessHours() - 1; }
      businessHours.remove(aBusinessHour);
      businessHours.add(index, aBusinessHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBusinessHourAt(BusinessHour aBusinessHour, int index)
  {
    boolean wasAdded = false;
    if(businessHours.contains(aBusinessHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBusinessHours()) { index = numberOfBusinessHours() - 1; }
      businessHours.remove(aBusinessHour);
      businessHours.add(index, aBusinessHour);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBusinessHourAt(aBusinessHour, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Owner existingOwner = owner;
    owner = null;
    if (existingOwner != null)
    {
      existingOwner.delete();
    }
    for(int i=businessHours.size(); i > 0; i--)
    {
      BusinessHour aBusinessHour = businessHours.get(i - 1);
      aBusinessHour.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "ticketFee" + ":" + getTicketFee()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "owner = "+(getOwner()!=null?Integer.toHexString(System.identityHashCode(getOwner())):"null");
  }
}