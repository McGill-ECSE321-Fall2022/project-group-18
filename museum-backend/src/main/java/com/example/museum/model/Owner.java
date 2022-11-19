package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/

/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

// line 34 "model.ump"
// line 122 "model.ump"
@Entity
public class Owner extends Person {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Owner Associations
  @OneToOne
  private Business business;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Owner() {
  }

  public Owner(int aAccountID, String aUsername, String aPassword, Business aBusiness, String aFirstName, String aLastName) {
    super(aAccountID, aUsername, aPassword, aFirstName, aLastName);
    if (!setBusiness(aBusiness)) {
      throw new RuntimeException(
          "Unable to create Owner due to aBusiness. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------
  /* Code from template association_GetOne */
  public Business getBusiness() {
    return business;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setBusiness(Business aNewBusiness) {
    boolean wasSet = false;
    if (aNewBusiness != null) {
      business = aNewBusiness;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    business = null;
    super.delete();
  }

}