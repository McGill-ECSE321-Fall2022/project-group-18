package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.*;
import java.util.*;

// line 28 "model.ump"
// line 61 "model.ump"
@Entity
public class Loan
{

    //------------------------
    // ENUMERATIONS
    //------------------------

    public enum ArtType { Painting, Sculpture }

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Loan Attributes
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int requestID;
    private int loanFee;
    private boolean approved;

    //Loan Associations
    @ManyToMany
    private List<Artifact> requestedArtifacts;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Loan() {}

    public Loan(int aRequestID, int aLoanFee, boolean aApproved, Artifact... allRequestedArtifacts)
    {
        requestID = aRequestID;
        loanFee = aLoanFee;
        approved = aApproved;
        requestedArtifacts = new ArrayList<Artifact>();
        boolean didAddRequestedArtifacts = setRequestedArtifacts(allRequestedArtifacts);
        if (!didAddRequestedArtifacts)
        {
            throw new RuntimeException("Unable to create Loan, must have 1 to 5 requestedArtifacts. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    public void setNewrequestedArtifactsList() {
        this.requestedArtifacts = new ArrayList<>();
    }

    public boolean setRequestID(int aRequestID)
    {
        boolean wasSet = false;
        requestID = aRequestID;
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

    public boolean setApproved(boolean aApproved)
    {
        boolean wasSet = false;
        approved = aApproved;
        wasSet = true;
        return wasSet;
    }

    public int getRequestID()
    {
        return requestID;
    }

    public int getLoanFee()
    {
        return loanFee;
    }

    public boolean getApproved()
    {
        return approved;
    }
    /* Code from template association_GetMany */
    public Artifact getRequestedArtifact(int index)
    {
        Artifact aRequestedArtifact = requestedArtifacts.get(index);
        return aRequestedArtifact;
    }

    public List<Artifact> getRequestedArtifacts()
    {
        List<Artifact> newRequestedArtifacts = Collections.unmodifiableList(requestedArtifacts);
        return newRequestedArtifacts;
    }

    public int numberOfRequestedArtifacts()
    {
        int number = requestedArtifacts.size();
        return number;
    }

    public boolean hasRequestedArtifacts()
    {
        boolean has = requestedArtifacts.size() > 0;
        return has;
    }

    public int indexOfRequestedArtifact(Artifact aRequestedArtifact)
    {
        int index = requestedArtifacts.indexOf(aRequestedArtifact);
        return index;
    }
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfRequestedArtifacts()
    {
        return 1;
    }
    /* Code from template association_MaximumNumberOfMethod */
    public static int maximumNumberOfRequestedArtifacts()
    {
        return 5;
    }
    /* Code from template association_AddUnidirectionalMN */
    public boolean addRequestedArtifact(Artifact aRequestedArtifact)
    {
        boolean wasAdded = false;
        if (requestedArtifacts.contains(aRequestedArtifact)) { return false; }
        if (numberOfRequestedArtifacts() < maximumNumberOfRequestedArtifacts())
        {
            requestedArtifacts.add(aRequestedArtifact);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean removeRequestedArtifact(Artifact aRequestedArtifact)
    {
        boolean wasRemoved = false;
        if (!requestedArtifacts.contains(aRequestedArtifact))
        {
            return wasRemoved;
        }

        if (numberOfRequestedArtifacts() <= minimumNumberOfRequestedArtifacts())
        {
            return wasRemoved;
        }

        requestedArtifacts.remove(aRequestedArtifact);
        wasRemoved = true;
        return wasRemoved;
    }
    /* Code from template association_SetUnidirectionalMN */
    public boolean setRequestedArtifacts(Artifact... newRequestedArtifacts)
    {
        boolean wasSet = false;
        ArrayList<Artifact> verifiedRequestedArtifacts = new ArrayList<Artifact>();
        for (Artifact aRequestedArtifact : newRequestedArtifacts)
        {
            if (verifiedRequestedArtifacts.contains(aRequestedArtifact))
            {
                continue;
            }
            verifiedRequestedArtifacts.add(aRequestedArtifact);
        }

        if (verifiedRequestedArtifacts.size() != newRequestedArtifacts.length || verifiedRequestedArtifacts.size() < minimumNumberOfRequestedArtifacts() || verifiedRequestedArtifacts.size() > maximumNumberOfRequestedArtifacts())
        {
            return wasSet;
        }

        requestedArtifacts.clear();
        requestedArtifacts.addAll(verifiedRequestedArtifacts);
        wasSet = true;
        return wasSet;
    }
    /* Code from template association_AddIndexControlFunctions */
    public boolean addRequestedArtifactAt(Artifact aRequestedArtifact, int index)
    {
        boolean wasAdded = false;
        if(addRequestedArtifact(aRequestedArtifact))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfRequestedArtifacts()) { index = numberOfRequestedArtifacts() - 1; }
            requestedArtifacts.remove(aRequestedArtifact);
            requestedArtifacts.add(index, aRequestedArtifact);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveRequestedArtifactAt(Artifact aRequestedArtifact, int index)
    {
        boolean wasAdded = false;
        if(requestedArtifacts.contains(aRequestedArtifact))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfRequestedArtifacts()) { index = numberOfRequestedArtifacts() - 1; }
            requestedArtifacts.remove(aRequestedArtifact);
            requestedArtifacts.add(index, aRequestedArtifact);
            wasAdded = true;
        }
        else
        {
            wasAdded = addRequestedArtifactAt(aRequestedArtifact, index);
        }
        return wasAdded;
    }

    public void delete()
    {
        requestedArtifacts.clear();
    }


    public String toString()
    {
        return super.toString() + "["+
                "requestID" + ":" + getRequestID()+ "," +
                "loanFee" + ":" + getLoanFee()+ "," +
                "approved" + ":" + getApproved()+ "]";
    }
}