package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// line 14 "model.ump"
// line 101 "model.ump"
@Entity
public class LoanedArtifact extends ArtifactAbs
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //LoanedArtifact Attributes
  private int loanFee;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public LoanedArtifact() {}

  public LoanedArtifact(String aName, ArtType aType, int aArtID, boolean aLoanable, int aLoanFee)
  {
    super(aName, aType, aArtID, aLoanable);
    loanFee = aLoanFee;
  }

  //------------------------
  // INTERFACE
  //------------------------


  public boolean setLoanFee(int aLoanFee)
  {
    boolean wasSet = false;
    loanFee = aLoanFee;
    wasSet = true;
    return wasSet;
  }


  public int getLoanFee()
  {
    return loanFee;
  }

  public void delete()
  {
    super.delete();
  }


}