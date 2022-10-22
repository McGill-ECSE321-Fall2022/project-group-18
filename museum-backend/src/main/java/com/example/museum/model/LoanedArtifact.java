package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



// line 14 "model.ump"
// line 104 "model.ump"
public class LoanedArtifact extends Artifact
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //LoanedArtifact Attributes
  private int loanID;
  private int loanFee;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public LoanedArtifact(String aName, ArtType aType, int aArtID, boolean aLoanable, int aLoanID, int aLoanFee)
  {
    super(aName, aType, aArtID, aLoanable);
    loanID = aLoanID;
    loanFee = aLoanFee;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLoanID(int aLoanID)
  {
    boolean wasSet = false;
    loanID = aLoanID;
    wasSet = true;
    return wasSet;
  }

  public boolean setLoanFee(int aLoanFee)
  {
    boolean wasSet = false;
    loanFee = aLoanFee;
    wasSet = true;
    return wasSet;
  }

  public int getLoanID()
  {
    return loanID;
  }

  public int getLoanFee()
  {
    return loanFee;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "loanID" + ":" + getLoanID()+ "," +
            "loanFee" + ":" + getLoanFee()+ "]";
  }
}