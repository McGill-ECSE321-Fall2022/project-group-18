package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// line 6 "model.ump"
// line 106 "model.ump"
@Entity
public class Artifact
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ArtType { Painting, Sculpture }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Artifact Attributes
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int artID;
  private String name;
  private ArtType type;
  private boolean loanable;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Artifact() {}
  public Artifact(String aName, ArtType aType, int aArtID, boolean aLoanable)
  {
    name = aName;
    type = aType;
    artID = aArtID;
    loanable = aLoanable;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setType(ArtType aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }

  public boolean setArtID(int aArtID)
  {
    boolean wasSet = false;
    artID = aArtID;
    wasSet = true;
    return wasSet;
  }

  public boolean setLoanable(boolean aLoanable)
  {
    boolean wasSet = false;
    loanable = aLoanable;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public ArtType getType()
  {
    return type;
  }

  public int getArtID()
  {
    return artID;
  }

  /**
   * boolean loaned;
   */
  public boolean getLoanable()
  {
    return loanable;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "artID" + ":" + getArtID()+ "," +
            "loanable" + ":" + getLoanable()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "type" + "=" + (getType() != null ? !getType().equals(this)  ? getType().toString().replaceAll("  ","    ") : "this" : "null");
  }
}