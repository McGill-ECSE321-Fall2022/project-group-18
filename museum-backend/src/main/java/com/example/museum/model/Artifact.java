/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/
package com.example.museum.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * namespace [folder for generated code]
 */
// line 4 "model.ump"
// line 98 "model.ump"
public class Artifact
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Artifact Attributes
  private int artifactID;
  private boolean loanable;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Artifact(int aArtifactID, boolean aLoanable)
  {
    artifactID = aArtifactID;
    loanable = aLoanable;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setArtifactID(int aArtifactID)
  {
    boolean wasSet = false;
    artifactID = aArtifactID;
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

  public int getArtifactID()
  {
    return artifactID;
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
            "artifactID" + ":" + getArtifactID()+ "," +
            "loanable" + ":" + getLoanable()+ "]";
  }
}