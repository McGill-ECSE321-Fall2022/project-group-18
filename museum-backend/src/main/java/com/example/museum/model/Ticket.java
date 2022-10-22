package main.java.com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.sql.Date;

// line 66 "model.ump"
// line 152 "model.ump"
public class Ticket
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Ticket Attributes
  private Date day;
  private int ticketID;
  private int price;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Ticket(Date aDay, int aTicketID, int aPrice)
  {
    day = aDay;
    ticketID = aTicketID;
    price = aPrice;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDay(Date aDay)
  {
    boolean wasSet = false;
    day = aDay;
    wasSet = true;
    return wasSet;
  }

  public boolean setTicketID(int aTicketID)
  {
    boolean wasSet = false;
    ticketID = aTicketID;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(int aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public Date getDay()
  {
    return day;
  }

  public int getTicketID()
  {
    return ticketID;
  }

  public int getPrice()
  {
    return price;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "ticketID" + ":" + getTicketID()+ "," +
            "price" + ":" + getPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "day" + "=" + (getDay() != null ? !getDay().equals(this)  ? getDay().toString().replaceAll("  ","    ") : "this" : "null");
  }
}