package com.example.museum.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * namespace [folder for generated code]
 */
// line 4 "model.ump"
// line 96 "model.ump"
@Entity
public class Artifact
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Artifact Attributes
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int artID;
  private boolean loanable;

  //------------------------
  // CONSTRUCTOR
  //------------------------


  public Artifact() {}

  public Artifact(int aArtID, boolean aLoanable)
  {
    artID = aArtID;
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
            "artID" + ":" + getArtID()+ "," +
            "loanable" + ":" + getLoanable()+ "]";
  }
}

