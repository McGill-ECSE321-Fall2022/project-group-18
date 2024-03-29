package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

// line 40 "model.ump"
@Entity
public class Ticket
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Ticket Attributes
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int ticketID;
  private Date day;
  private int price;



  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Ticket() {

  }

  public Ticket(int aTicketID, Date aDay, int aPrice)
  {
    ticketID = aTicketID;
    day = aDay;
    price = aPrice;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTicketID(int aTicketID)
  {
    boolean wasSet = false;
    ticketID = aTicketID;
    wasSet = true;
    return wasSet;
  }

  public boolean setDay(Date aDay)
  {
    boolean wasSet = false;
    day = aDay;
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

  public int getTicketID()
  {
    return ticketID;
  }

  public Date getDay()
  {
    return day;
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