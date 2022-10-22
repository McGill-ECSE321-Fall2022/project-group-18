package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;

// line 67 "model.ump"
// line 160 "model.ump"
@Entity
public class BusinessHour
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //BusinessHour Attributes
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int businessHourID;
  private Date day;
  private Time openTime;
  private Time closeTime;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public BusinessHour(int aBusinessHourID, Date aDay, Time aOpenTime, Time aCloseTime)
  {
    businessHourID = aBusinessHourID;
    day = aDay;
    openTime = aOpenTime;
    closeTime = aCloseTime;
  }

  public BusinessHour() {

  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setBusinessHourID(int aBusinessHourID)
  {
    boolean wasSet = false;
    businessHourID = aBusinessHourID;
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

  public boolean setOpenTime(Time aOpenTime)
  {
    boolean wasSet = false;
    openTime = aOpenTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setCloseTime(Time aCloseTime)
  {
    boolean wasSet = false;
    closeTime = aCloseTime;
    wasSet = true;
    return wasSet;
  }

  public int getBusinessHourID()
  {
    return businessHourID;
  }

  public Date getDay()
  {
    return day;
  }

  public Time getOpenTime()
  {
    return openTime;
  }

  public Time getCloseTime()
  {
    return closeTime;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "businessHourID" + ":" + getBusinessHourID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "day" + "=" + (getDay() != null ? !getDay().equals(this)  ? getDay().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "openTime" + "=" + (getOpenTime() != null ? !getOpenTime().equals(this)  ? getOpenTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "closeTime" + "=" + (getCloseTime() != null ? !getCloseTime().equals(this)  ? getCloseTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}