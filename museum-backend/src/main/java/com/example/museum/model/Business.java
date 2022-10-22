package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 73 "model.ump"
// line 157 "model.ump"
public class Business
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Business Attributes
  private int ticketFee;

  //Business Associations
  private List<BusinessHour> businessHours;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Business(int aTicketFee)
  {
    ticketFee = aTicketFee;
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
  /* Code from template association_AddUnidirectionalMany */
  public boolean addBusinessHour(BusinessHour aBusinessHour)
  {
    boolean wasAdded = false;
    if (businessHours.contains(aBusinessHour)) { return false; }
    businessHours.add(aBusinessHour);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBusinessHour(BusinessHour aBusinessHour)
  {
    boolean wasRemoved = false;
    if (businessHours.contains(aBusinessHour))
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
    businessHours.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "ticketFee" + ":" + getTicketFee()+ "]";
  }
}