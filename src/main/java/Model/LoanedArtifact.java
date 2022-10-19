package Model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



// line 11 "model.ump"
// line 102 "model.ump"
public class LoanedArtifact extends Artifact
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //LoanedArtifact Attributes
    private int loanFee;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public LoanedArtifact(String aArtID, boolean aLoanable, int aLoanFee)
    {
        super(aArtID, aLoanable);
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


    public String toString()
    {
        return super.toString() + "["+
                "loanFee" + ":" + getLoanFee()+ "]";
    }
}
