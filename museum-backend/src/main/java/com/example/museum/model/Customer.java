package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/

/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

import java.util.*;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

// line 49 "model.ump"
// line 140 "model.ump"
@Entity
public class Customer extends Person {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Customer Associations
  @OneToMany
  private List<LoanedArtifact> loanedArtifacts;
  @OneToMany
  private List<Donation> customerDonatedArtifacts;
  @OneToOne
  private LoanRequest loanRequest;

  @OneToMany
  private List<Ticket> customerTickets;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Customer() {
  }

  public Customer(int aAccountID, String aUsername, String aPassword, LoanRequest aLoanRequest) {
    super(aAccountID, aUsername, aPassword);
    loanedArtifacts = new ArrayList<LoanedArtifact>();
    customerDonatedArtifacts = new ArrayList<Donation>();
    if (!setLoanRequest(aLoanRequest)) {
      throw new RuntimeException(
          "Unable to create Customer due to aLoanRequest. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    customerTickets = new ArrayList<Ticket>();
  }

  // ------------------------
  // INTERFACE
  // ------------------------
  /* Code from template association_GetMany */

  public void setLoanedArtifactsList() {
    this.loanedArtifacts = new ArrayList<LoanedArtifact>();
  }

  public void setCustomerDonatedArtifactsList() {
    this.customerDonatedArtifacts = new ArrayList<Donation>();
  }

  public void setCustomerTicketsList() {
    this.customerTickets = new ArrayList<Ticket>();
  }

  public LoanedArtifact getLoanedArtifact(int index) {
    LoanedArtifact aLoanedArtifact = loanedArtifacts.get(index);
    return aLoanedArtifact;
  }

  public List<LoanedArtifact> getLoanedArtifacts() {
    List<LoanedArtifact> newLoanedArtifacts = Collections.unmodifiableList(loanedArtifacts);
    return newLoanedArtifacts;
  }

  public int numberOfLoanedArtifacts() {
    int number = loanedArtifacts.size();
    return number;
  }

  public boolean hasLoanedArtifacts() {
    boolean has = loanedArtifacts.size() > 0;
    return has;
  }

  public int indexOfLoanedArtifact(LoanedArtifact aLoanedArtifact) {
    int index = loanedArtifacts.indexOf(aLoanedArtifact);
    return index;
  }

  /* Code from template association_GetMany */
  public Donation getCustomerDonatedArtifact(int index) {
    Donation aCustomerDonatedArtifact = customerDonatedArtifacts.get(index);
    return aCustomerDonatedArtifact;
  }

  public List<Donation> getCustomerDonatedArtifacts() {
    List<Donation> newCustomerDonatedArtifacts = Collections.unmodifiableList(customerDonatedArtifacts);
    return newCustomerDonatedArtifacts;
  }

  public int numberOfCustomerDonatedArtifacts() {
    int number = customerDonatedArtifacts.size();
    return number;
  }

  public boolean hasCustomerDonatedArtifacts() {
    boolean has = customerDonatedArtifacts.size() > 0;
    return has;
  }

  public int indexOfCustomerDonatedArtifact(Donation aCustomerDonatedArtifact) {
    int index = customerDonatedArtifacts.indexOf(aCustomerDonatedArtifact);
    return index;
  }

  /* Code from template association_GetOne */
  public LoanRequest getLoanRequest() {
    return loanRequest;
  }

  /* Code from template association_GetMany */
  public Ticket getCustomerTicket(int index) {
    Ticket aCustomerTicket = customerTickets.get(index);
    return aCustomerTicket;
  }

  public List<Ticket> getCustomerTickets() {
    List<Ticket> newCustomerTickets = Collections.unmodifiableList(customerTickets);
    return newCustomerTickets;
  }

  public int numberOfCustomerTickets() {
    int number = customerTickets.size();
    return number;
  }

  public boolean hasCustomerTickets() {
    boolean has = customerTickets.size() > 0;
    return has;
  }

  public int indexOfCustomerTicket(Ticket aCustomerTicket) {
    int index = customerTickets.indexOf(aCustomerTicket);
    return index;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLoanedArtifacts() {
    return 0;
  }

  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfLoanedArtifacts() {
    return 5;
  }

  /* Code from template association_AddUnidirectionalOptionalN */
  public boolean addLoanedArtifact(LoanedArtifact aLoanedArtifact) {
    boolean wasAdded = false;
    if (loanedArtifacts.contains(aLoanedArtifact)) {
      return false;
    }
    if (numberOfLoanedArtifacts() < maximumNumberOfLoanedArtifacts()) {
      loanedArtifacts.add(aLoanedArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean removeLoanedArtifact(LoanedArtifact aLoanedArtifact) {
    boolean wasRemoved = false;
    if (loanedArtifacts.contains(aLoanedArtifact)) {
      loanedArtifacts.remove(aLoanedArtifact);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_SetUnidirectionalOptionalN */
  public boolean setLoanedArtifacts(LoanedArtifact... newLoanedArtifacts) {
    boolean wasSet = false;
    ArrayList<LoanedArtifact> verifiedLoanedArtifacts = new ArrayList<LoanedArtifact>();
    for (LoanedArtifact aLoanedArtifact : newLoanedArtifacts) {
      if (verifiedLoanedArtifacts.contains(aLoanedArtifact)) {
        continue;
      }
      verifiedLoanedArtifacts.add(aLoanedArtifact);
    }

    if (verifiedLoanedArtifacts.size() != newLoanedArtifacts.length
        || verifiedLoanedArtifacts.size() > maximumNumberOfLoanedArtifacts()) {
      return wasSet;
    }

    loanedArtifacts.clear();
    loanedArtifacts.addAll(verifiedLoanedArtifacts);
    wasSet = true;
    return wasSet;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addLoanedArtifactAt(LoanedArtifact aLoanedArtifact, int index) {
    boolean wasAdded = false;
    if (addLoanedArtifact(aLoanedArtifact)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfLoanedArtifacts()) {
        index = numberOfLoanedArtifacts() - 1;
      }
      loanedArtifacts.remove(aLoanedArtifact);
      loanedArtifacts.add(index, aLoanedArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLoanedArtifactAt(LoanedArtifact aLoanedArtifact, int index) {
    boolean wasAdded = false;
    if (loanedArtifacts.contains(aLoanedArtifact)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfLoanedArtifacts()) {
        index = numberOfLoanedArtifacts() - 1;
      }
      loanedArtifacts.remove(aLoanedArtifact);
      loanedArtifacts.add(index, aLoanedArtifact);
      wasAdded = true;
    } else {
      wasAdded = addLoanedArtifactAt(aLoanedArtifact, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomerDonatedArtifacts() {
    return 0;
  }

  /* Code from template association_AddUnidirectionalMany */
  public boolean addCustomerDonatedArtifact(Donation aCustomerDonatedArtifact) {
    boolean wasAdded = false;
    if (customerDonatedArtifacts.contains(aCustomerDonatedArtifact)) {
      return false;
    }
    customerDonatedArtifacts.add(aCustomerDonatedArtifact);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCustomerDonatedArtifact(Donation aCustomerDonatedArtifact) {
    boolean wasRemoved = false;
    if (customerDonatedArtifacts.contains(aCustomerDonatedArtifact)) {
      customerDonatedArtifacts.remove(aCustomerDonatedArtifact);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomerDonatedArtifactAt(Donation aCustomerDonatedArtifact, int index) {
    boolean wasAdded = false;
    if (addCustomerDonatedArtifact(aCustomerDonatedArtifact)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfCustomerDonatedArtifacts()) {
        index = numberOfCustomerDonatedArtifacts() - 1;
      }
      customerDonatedArtifacts.remove(aCustomerDonatedArtifact);
      customerDonatedArtifacts.add(index, aCustomerDonatedArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerDonatedArtifactAt(Donation aCustomerDonatedArtifact, int index) {
    boolean wasAdded = false;
    if (customerDonatedArtifacts.contains(aCustomerDonatedArtifact)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfCustomerDonatedArtifacts()) {
        index = numberOfCustomerDonatedArtifacts() - 1;
      }
      customerDonatedArtifacts.remove(aCustomerDonatedArtifact);
      customerDonatedArtifacts.add(index, aCustomerDonatedArtifact);
      wasAdded = true;
    } else {
      wasAdded = addCustomerDonatedArtifactAt(aCustomerDonatedArtifact, index);
    }
    return wasAdded;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setLoanRequest(LoanRequest aNewLoanRequest) {
    boolean wasSet = false;
    if (aNewLoanRequest != null) {
      loanRequest = aNewLoanRequest;
      wasSet = true;
    }
    return wasSet;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomerTickets() {
    return 0;
  }

  /* Code from template association_AddUnidirectionalMany */
  public boolean addCustomerTicket(Ticket aCustomerTicket) {
    boolean wasAdded = false;
    if (customerTickets.contains(aCustomerTicket)) {
      return false;
    }
    customerTickets.add(aCustomerTicket);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCustomerTicket(Ticket aCustomerTicket) {
    boolean wasRemoved = false;
    if (customerTickets.contains(aCustomerTicket)) {
      customerTickets.remove(aCustomerTicket);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomerTicketAt(Ticket aCustomerTicket, int index) {
    boolean wasAdded = false;
    if (addCustomerTicket(aCustomerTicket)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfCustomerTickets()) {
        index = numberOfCustomerTickets() - 1;
      }
      customerTickets.remove(aCustomerTicket);
      customerTickets.add(index, aCustomerTicket);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerTicketAt(Ticket aCustomerTicket, int index) {
    boolean wasAdded = false;
    if (customerTickets.contains(aCustomerTicket)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfCustomerTickets()) {
        index = numberOfCustomerTickets() - 1;
      }
      customerTickets.remove(aCustomerTicket);
      customerTickets.add(index, aCustomerTicket);
      wasAdded = true;
    } else {
      wasAdded = addCustomerTicketAt(aCustomerTicket, index);
    }
    return wasAdded;
  }

  public void delete() {
    loanedArtifacts.clear();
    customerDonatedArtifacts.clear();
    loanRequest = null;
    customerTickets.clear();
    super.delete();
  }

}