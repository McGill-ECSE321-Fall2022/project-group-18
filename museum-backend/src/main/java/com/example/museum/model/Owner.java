package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.Entity;

// line 42 "model.ump"
// line 130 "model.ump"
@Entity
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

  public Owner(String aPersonID, String aUsername, String aPassword, Business aBusiness)
  {
    super(aPersonID, aUsername, aPassword);
    if (!setBusiness(aBusiness))
    {
      throw new RuntimeException("Unable to create Owner due to aBusiness. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Business getBusiness()
  {
    return business;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setBusiness(Business aNewBusiness)
  {
    boolean wasSet = false;
    if (aNewBusiness != null)
    {
      business = aNewBusiness;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    business = null;
    super.delete();
  }

}