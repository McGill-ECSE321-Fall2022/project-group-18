package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/

/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.*;
import java.sql.Date;

// line 36 "model.ump"
@Entity
public class Customer extends Person {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Customer Associations
  @OneToMany
  private List<Donation> customerDonatedArtifacts;
  @OneToMany
  private List<Loan> loans;
  @OneToMany
  private List<Ticket> customerTickets;

  private int credit;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Customer() {
    customerDonatedArtifacts = new ArrayList<Donation>();
    loans = new ArrayList<Loan>();
    customerTickets = new ArrayList<Ticket>();
  }

  public Customer(int aAccountID, String aUsername, String aPassword, String aFirstName, String aLastName,
      int aCredit) {
    super(aAccountID, aUsername, aPassword, aFirstName, aLastName);
    credit = aCredit;
    customerDonatedArtifacts = new ArrayList<Donation>();
    loans = new ArrayList<Loan>();
    customerTickets = new ArrayList<Ticket>();
  }

  // ------------------------
  // INTERFACE
  // ------------------------
  /* Code from template association_GetMany */

  public void setCustomerTicketsList() {
    this.customerTickets = new ArrayList<>();
  }

  public void setCustomerDonatedArtifactsList() {
    this.customerDonatedArtifacts = new ArrayList<>();
  }

  public void setLoansList() {
    this.loans = new ArrayList<>();
  }

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

  /* Code from template association_GetMany */
  public Loan getLoan(int index) {
    Loan aLoan = loans.get(index);
    return aLoan;
  }

  public List<Loan> getLoans() {
    List<Loan> newLoans = Collections.unmodifiableList(loans);
    return newLoans;
  }

  public int numberOfLoans() {
    int number = loans.size();
    return number;
  }

  public boolean hasLoans() {
    boolean has = loans.size() > 0;
    return has;
  }

  public int indexOfLoan(Loan aLoan) {
    int index = loans.indexOf(aLoan);
    return index;
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

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLoans() {
    return 0;
  }

  /* Code from template association_AddUnidirectionalMany */
  public boolean addLoan(Loan aLoan) {
    boolean wasAdded = false;
    if (loans.contains(aLoan)) {
      return false;
    }
    loans.add(aLoan);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLoan(Loan aLoan) {
    boolean wasRemoved = false;
    if (loans.contains(aLoan)) {
      loans.remove(aLoan);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addLoanAt(Loan aLoan, int index) {
    boolean wasAdded = false;
    if (addLoan(aLoan)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfLoans()) {
        index = numberOfLoans() - 1;
      }
      loans.remove(aLoan);
      loans.add(index, aLoan);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLoanAt(Loan aLoan, int index) {
    boolean wasAdded = false;
    if (loans.contains(aLoan)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfLoans()) {
        index = numberOfLoans() - 1;
      }
      loans.remove(aLoan);
      loans.add(index, aLoan);
      wasAdded = true;
    } else {
      wasAdded = addLoanAt(aLoan, index);
    }
    return wasAdded;
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

  public boolean setCredit(int aCredit) {
    boolean wasSet = false;
    credit = aCredit;
    wasSet = true;
    return wasSet;
  }

  public int getCredit() {
    return credit;
  }

  public void delete() {
    customerDonatedArtifacts.clear();
    loans.clear();
    customerTickets.clear();
    super.delete();
  }

}