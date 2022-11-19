package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.*;
import java.util.*;

// line 14 "model.ump"
@Entity
public class Donation
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ArtType { Painting, Sculpture }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Donation Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int donationID;

  //Donation Associations
  @OneToMany
  private List<Artifact> donatedArtifacts;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Donation() {}

  public Donation(int aDonationID)
  {
    donationID = aDonationID;
    donatedArtifacts = new ArrayList<Artifact>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void setNewDonationArtifactsList() {
    this.donatedArtifacts = new ArrayList<>();
  }

  public boolean setDonationID(int aDonationID)
  {
    boolean wasSet = false;
    donationID = aDonationID;
    wasSet = true;
    return wasSet;
  }

  public int getDonationID()
  {
    return donationID;
  }
  /* Code from template association_GetMany */
  public Artifact getDonatedArtifact(int index)
  {
    Artifact aDonatedArtifact = donatedArtifacts.get(index);
    return aDonatedArtifact;
  }

  public List<Artifact> getDonatedArtifacts()
  {
    List<Artifact> newDonatedArtifacts = Collections.unmodifiableList(donatedArtifacts);
    return newDonatedArtifacts;
  }

  public int numberOfDonatedArtifacts()
  {
    int number = donatedArtifacts.size();
    return number;
  }

  public boolean hasDonatedArtifacts()
  {
    boolean has = donatedArtifacts.size() > 0;
    return has;
  }

  public int indexOfDonatedArtifact(Artifact aDonatedArtifact)
  {
    int index = donatedArtifacts.indexOf(aDonatedArtifact);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDonatedArtifacts()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addDonatedArtifact(Artifact aDonatedArtifact)
  {
    boolean wasAdded = false;
    if (donatedArtifacts.contains(aDonatedArtifact)) { return false; }
    donatedArtifacts.add(aDonatedArtifact);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDonatedArtifact(Artifact aDonatedArtifact)
  {
    boolean wasRemoved = false;
    if (donatedArtifacts.contains(aDonatedArtifact))
    {
      donatedArtifacts.remove(aDonatedArtifact);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addDonatedArtifactAt(Artifact aDonatedArtifact, int index)
  {
    boolean wasAdded = false;
    if(addDonatedArtifact(aDonatedArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDonatedArtifacts()) { index = numberOfDonatedArtifacts() - 1; }
      donatedArtifacts.remove(aDonatedArtifact);
      donatedArtifacts.add(index, aDonatedArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDonatedArtifactAt(Artifact aDonatedArtifact, int index)
  {
    boolean wasAdded = false;
    if(donatedArtifacts.contains(aDonatedArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDonatedArtifacts()) { index = numberOfDonatedArtifacts() - 1; }
      donatedArtifacts.remove(aDonatedArtifact);
      donatedArtifacts.add(index, aDonatedArtifact);
      wasAdded = true;
    }
    else
    {
      wasAdded = addDonatedArtifactAt(aDonatedArtifact, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    donatedArtifacts.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "donationID" + ":" + getDonationID()+ "]";
  }
}