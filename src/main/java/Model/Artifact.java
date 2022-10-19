package Model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



/**
 * namespace [folder for generated code]
 */
// line 4 "model.ump"
// line 96 "model.ump"
public class Artifact
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Artifact Attributes
  private String artID;
  private boolean loanable;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Artifact(String aArtID, boolean aLoanable)
  {
    artID = aArtID;
    loanable = aLoanable;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setArtID(String aArtID)
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

  public String getArtID()
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

