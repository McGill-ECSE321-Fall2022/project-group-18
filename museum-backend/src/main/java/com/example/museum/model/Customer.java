package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.Entity;
import java.util.*;
import java.sql.Date;

// line 61 "model.ump"
// line 147 "model.ump"

public class Customer extends Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Associations
  private List<LoanedArtifact> loanedArtifacts;
  private List<Donation> donations;
  private LoanRequest loanRequest;
  private List<Ticket> tickets;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(String aPersonID, String aUsername, String aPassword, LoanRequest aLoanRequest)
  {
    super(aPersonID, aUsername, aPassword);
    loanedArtifacts = new ArrayList<LoanedArtifact>();
    donations = new ArrayList<Donation>();
    if (!setLoanRequest(aLoanRequest))
    {
      throw new RuntimeException("Unable to create Customer due to aLoanRequest. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    tickets = new ArrayList<Ticket>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public LoanedArtifact getLoanedArtifact(int index)
  {
    LoanedArtifact aLoanedArtifact = loanedArtifacts.get(index);
    return aLoanedArtifact;
  }

  public List<LoanedArtifact> getLoanedArtifacts()
  {
    List<LoanedArtifact> newLoanedArtifacts = Collections.unmodifiableList(loanedArtifacts);
    return newLoanedArtifacts;
  }

  public int numberOfLoanedArtifacts()
  {
    int number = loanedArtifacts.size();
    return number;
  }

  public boolean hasLoanedArtifacts()
  {
    boolean has = loanedArtifacts.size() > 0;
    return has;
  }

  public int indexOfLoanedArtifact(LoanedArtifact aLoanedArtifact)
  {
    int index = loanedArtifacts.indexOf(aLoanedArtifact);
    return index;
  }
  /* Code from template association_GetMany */
  public Donation getDonation(int index)
  {
    Donation aDonation = donations.get(index);
    return aDonation;
  }

  public List<Donation> getDonations()
  {
    List<Donation> newDonations = Collections.unmodifiableList(donations);
    return newDonations;
  }

  public int numberOfDonations()
  {
    int number = donations.size();
    return number;
  }

  public boolean hasDonations()
  {
    boolean has = donations.size() > 0;
    return has;
  }

  public int indexOfDonation(Donation aDonation)
  {
    int index = donations.indexOf(aDonation);
    return index;
  }
  /* Code from template association_GetOne */
  public LoanRequest getLoanRequest()
  {
    return loanRequest;
  }
  /* Code from template association_GetMany */
  public Ticket getTicket(int index)
  {
    Ticket aTicket = tickets.get(index);
    return aTicket;
  }

  public List<Ticket> getTickets()
  {
    List<Ticket> newTickets = Collections.unmodifiableList(tickets);
    return newTickets;
  }

  public int numberOfTickets()
  {
    int number = tickets.size();
    return number;
  }

  public boolean hasTickets()
  {
    boolean has = tickets.size() > 0;
    return has;
  }

  public int indexOfTicket(Ticket aTicket)
  {
    int index = tickets.indexOf(aTicket);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLoanedArtifacts()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfLoanedArtifacts()
  {
    return 5;
  }
  /* Code from template association_AddUnidirectionalOptionalN */
  public boolean addLoanedArtifact(LoanedArtifact aLoanedArtifact)
  {
    boolean wasAdded = false;
    if (loanedArtifacts.contains(aLoanedArtifact)) { return false; }
    if (numberOfLoanedArtifacts() < maximumNumberOfLoanedArtifacts())
    {
      loanedArtifacts.add(aLoanedArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean removeLoanedArtifact(LoanedArtifact aLoanedArtifact)
  {
    boolean wasRemoved = false;
    if (loanedArtifacts.contains(aLoanedArtifact))
    {
      loanedArtifacts.remove(aLoanedArtifact);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_SetUnidirectionalOptionalN */
  public boolean setLoanedArtifacts(LoanedArtifact... newLoanedArtifacts)
  {
    boolean wasSet = false;
    ArrayList<LoanedArtifact> verifiedLoanedArtifacts = new ArrayList<LoanedArtifact>();
    for (LoanedArtifact aLoanedArtifact : newLoanedArtifacts)
    {
      if (verifiedLoanedArtifacts.contains(aLoanedArtifact))
      {
        continue;
      }
      verifiedLoanedArtifacts.add(aLoanedArtifact);
    }

    if (verifiedLoanedArtifacts.size() != newLoanedArtifacts.length || verifiedLoanedArtifacts.size() > maximumNumberOfLoanedArtifacts())
    {
      return wasSet;
    }

    loanedArtifacts.clear();
    loanedArtifacts.addAll(verifiedLoanedArtifacts);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLoanedArtifactAt(LoanedArtifact aLoanedArtifact, int index)
  {
    boolean wasAdded = false;
    if(addLoanedArtifact(aLoanedArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLoanedArtifacts()) { index = numberOfLoanedArtifacts() - 1; }
      loanedArtifacts.remove(aLoanedArtifact);
      loanedArtifacts.add(index, aLoanedArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLoanedArtifactAt(LoanedArtifact aLoanedArtifact, int index)
  {
    boolean wasAdded = false;
    if(loanedArtifacts.contains(aLoanedArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLoanedArtifacts()) { index = numberOfLoanedArtifacts() - 1; }
      loanedArtifacts.remove(aLoanedArtifact);
      loanedArtifacts.add(index, aLoanedArtifact);
      wasAdded = true;
    }
    else
    {
      wasAdded = addLoanedArtifactAt(aLoanedArtifact, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDonations()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addDonation(Donation aDonation)
  {
    boolean wasAdded = false;
    if (donations.contains(aDonation)) { return false; }
    donations.add(aDonation);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDonation(Donation aDonation)
  {
    boolean wasRemoved = false;
    if (donations.contains(aDonation))
    {
      donations.remove(aDonation);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addDonationAt(Donation aDonation, int index)
  {
    boolean wasAdded = false;
    if(addDonation(aDonation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDonations()) { index = numberOfDonations() - 1; }
      donations.remove(aDonation);
      donations.add(index, aDonation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDonationAt(Donation aDonation, int index)
  {
    boolean wasAdded = false;
    if(donations.contains(aDonation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDonations()) { index = numberOfDonations() - 1; }
      donations.remove(aDonation);
      donations.add(index, aDonation);
      wasAdded = true;
    }
    else
    {
      wasAdded = addDonationAt(aDonation, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setLoanRequest(LoanRequest aNewLoanRequest)
  {
    boolean wasSet = false;
    if (aNewLoanRequest != null)
    {
      loanRequest = aNewLoanRequest;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTickets()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addTicket(Ticket aTicket)
  {
    boolean wasAdded = false;
    if (tickets.contains(aTicket)) { return false; }
    tickets.add(aTicket);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTicket(Ticket aTicket)
  {
    boolean wasRemoved = false;
    if (tickets.contains(aTicket))
    {
      tickets.remove(aTicket);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTicketAt(Ticket aTicket, int index)
  {
    boolean wasAdded = false;
    if(addTicket(aTicket))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTickets()) { index = numberOfTickets() - 1; }
      tickets.remove(aTicket);
      tickets.add(index, aTicket);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTicketAt(Ticket aTicket, int index)
  {
    boolean wasAdded = false;
    if(tickets.contains(aTicket))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTickets()) { index = numberOfTickets() - 1; }
      tickets.remove(aTicket);
      tickets.add(index, aTicket);
      wasAdded = true;
    }
    else
    {
      wasAdded = addTicketAt(aTicket, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    loanedArtifacts.clear();
    donations.clear();
    loanRequest = null;
    tickets.clear();
    super.delete();
  }

}