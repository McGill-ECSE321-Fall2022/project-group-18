package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import javax.persistence.*;
import java.util.*;

// line 43 "model.ump"
// line 133 "model.ump"
@Entity
public class LoanRequest
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ArtType { Painting, Sculpture }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //LoanRequest Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int requestID;

  //LoanRequest Associations
  @OneToMany
  private List<Artifact> requestedArtifacts;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public LoanRequest() {}

  public LoanRequest(int aRequestID)
  {
    requestID = aRequestID;
    requestedArtifacts = new ArrayList<Artifact>();
}

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNewRequestedArtifactsList () {
    this.requestedArtifacts = new ArrayList<Artifact>();
    return true;
  }

  public boolean setRequestID(int aRequestID)
  {
    boolean wasSet = false;
    requestID = aRequestID;
    wasSet = true;
    return wasSet;
  }

  public int getRequestID()
  {
    return requestID;
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
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfRequestedArtifacts()
  {
    return 5;
  }
  /* Code from template association_AddUnidirectionalOptionalN */
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
    if (requestedArtifacts.contains(aRequestedArtifact))
    {
      requestedArtifacts.remove(aRequestedArtifact);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_SetUnidirectionalOptionalN */
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

    if (verifiedRequestedArtifacts.size() != newRequestedArtifacts.length || verifiedRequestedArtifacts.size() > maximumNumberOfRequestedArtifacts())
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
            "requestID" + ":" + getRequestID()+ "]";
  }
}