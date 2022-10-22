package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// line 6 "model.ump"
// line 94 "model.ump"
@Entity
public class Artifact
{
  public Artifact() {}

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

  public Artifact(int aArtID, String aName, ArtType aType, boolean aLoanable)
  {
    artID = aArtID;
    name = aName;
    type = aType;
    loanable = aLoanable;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setArtID(int aArtID)
  {
    boolean wasSet = false;
    artID = aArtID;
    wasSet = true;
    return wasSet;
  }

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

  public boolean setLoanable(boolean aLoanable)
  {
    boolean wasSet = false;
    loanable = aLoanable;
    wasSet = true;
    return wasSet;
  }

  public int getArtID()
  {
    return artID;
  }

  public String getName()
  {
    return name;
  }

  public ArtType getType()
  {
    return type;
  }

  public boolean getLoanable()
  {
    return loanable;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "artID" + ":" + getArtID()+ "," +
            "name" + ":" + getName()+ "," +
            "loanable" + ":" + getLoanable()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "type" + "=" + (getType() != null ? !getType().equals(this)  ? getType().toString().replaceAll("  ","    ") : "this" : "null");
  }
}