package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// line 74 "model.ump"
// line 165 "model.ump"
@Entity
public class EmployeeHour
{
  public EmployeeHour() {}

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //EmployeeHour Attributes
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int employeeHourID;
  private Date day;
  private Time startTime;
  private Time endTime;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public EmployeeHour(int aEmployeeHourID, Date aDay, Time aStartTime, Time aEndTime)
  {
    employeeHourID = aEmployeeHourID;
    day = aDay;
    startTime = aStartTime;
    endTime = aEndTime;
  }

  //------------------------
  // INTERFACE
  //------------------------

  

public boolean setEmployeeHourID(int aEmployeeHourID)
  {
    boolean wasSet = false;
    employeeHourID = aEmployeeHourID;
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

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime)
  {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public int getEmployeeHourID()
  {
    return employeeHourID;
  }

  public Date getDay()
  {
    return day;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "employeeHourID" + ":" + getEmployeeHourID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "day" + "=" + (getDay() != null ? !getDay().equals(this)  ? getDay().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}