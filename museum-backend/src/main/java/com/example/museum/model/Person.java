package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Inheritance;
import javax.persistence.Id;

// line 20 "model.ump"
@Entity
@Inheritance
public abstract class Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Person Attributes
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int accountID;
  private String username;
  private String password;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Person() {}

  public Person(int aAccountID, String aUsername, String aPassword)
  {
    accountID = aAccountID;
    username = aUsername;
    password = aPassword;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAccountID(int aAccountID)
  {
    boolean wasSet = false;
    accountID = aAccountID;
    wasSet = true;
    return wasSet;
  }

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

  public int getAccountID()
  {
    return accountID;
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
            "accountID" + ":" + getAccountID()+ "," +
            "username" + ":" + getUsername()+ "," +
            "password" + ":" + getPassword()+ "]";
  }
}