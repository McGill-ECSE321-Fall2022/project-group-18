package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;

// line 22 "model.ump"
// line 118 "model.ump"

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

  private int donationID;

  //Donation Associations
  private List<Artifact> artifacts;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Donation(int aDonationID)
  {
    donationID = aDonationID;
    artifacts = new ArrayList<Artifact>();
  }

  //------------------------
  // INTERFACE
  //------------------------

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
  public Artifact getArtifact(int index)
  {
    Artifact aArtifact = artifacts.get(index);
    return aArtifact;
  }

  public List<Artifact> getArtifacts()
  {
    List<Artifact> newArtifacts = Collections.unmodifiableList(artifacts);
    return newArtifacts;
  }

  public int numberOfArtifacts()
  {
    int number = artifacts.size();
    return number;
  }

  public boolean hasArtifacts()
  {
    boolean has = artifacts.size() > 0;
    return has;
  }

  public int indexOfArtifact(Artifact aArtifact)
  {
    int index = artifacts.indexOf(aArtifact);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfArtifacts()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addArtifact(Artifact aArtifact)
  {
    boolean wasAdded = false;
    if (artifacts.contains(aArtifact)) { return false; }
    artifacts.add(aArtifact);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeArtifact(Artifact aArtifact)
  {
    boolean wasRemoved = false;
    if (artifacts.contains(aArtifact))
    {
      artifacts.remove(aArtifact);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addArtifactAt(Artifact aArtifact, int index)
  {
    boolean wasAdded = false;
    if(addArtifact(aArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfArtifacts()) { index = numberOfArtifacts() - 1; }
      artifacts.remove(aArtifact);
      artifacts.add(index, aArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveArtifactAt(Artifact aArtifact, int index)
  {
    boolean wasAdded = false;
    if(artifacts.contains(aArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfArtifacts()) { index = numberOfArtifacts() - 1; }
      artifacts.remove(aArtifact);
      artifacts.add(index, aArtifact);
      wasAdded = true;
    }
    else
    {
      wasAdded = addArtifactAt(aArtifact, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    artifacts.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "donationID" + ":" + getDonationID()+ "]";
  }
}