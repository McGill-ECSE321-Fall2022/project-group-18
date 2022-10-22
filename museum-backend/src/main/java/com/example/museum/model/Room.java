package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.util.*;

// line 81 "model.ump"
// line 170 "model.ump"
public class Room
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ArtType { Painting, Sculpture }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Attributes
  private int roomID;
  private String name;
  private int capacity;
  private Artifact artifacts;

  //Room Associations
  private List<Artifact> roomArtifacts;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(int aRoomID, String aName, int aCapacity, Artifact aArtifacts)
  {
    roomID = aRoomID;
    name = aName;
    capacity = aCapacity;
    artifacts = aArtifacts;
    roomArtifacts = new ArrayList<Artifact>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRoomID(int aRoomID)
  {
    boolean wasSet = false;
    roomID = aRoomID;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setCapacity(int aCapacity)
  {
    boolean wasSet = false;
    capacity = aCapacity;
    wasSet = true;
    return wasSet;
  }

  public boolean setArtifacts(Artifact aArtifacts)
  {
    boolean wasSet = false;
    artifacts = aArtifacts;
    wasSet = true;
    return wasSet;
  }

  public int getRoomID()
  {
    return roomID;
  }

  public String getName()
  {
    return name;
  }

  public int getCapacity()
  {
    return capacity;
  }

  public Artifact getArtifacts()
  {
    return artifacts;
  }
  /* Code from template association_GetMany */
  public Artifact getRoomArtifact(int index)
  {
    Artifact aRoomArtifact = roomArtifacts.get(index);
    return aRoomArtifact;
  }

  public List<Artifact> getRoomArtifacts()
  {
    List<Artifact> newRoomArtifacts = Collections.unmodifiableList(roomArtifacts);
    return newRoomArtifacts;
  }

  public int numberOfRoomArtifacts()
  {
    int number = roomArtifacts.size();
    return number;
  }

  public boolean hasRoomArtifacts()
  {
    boolean has = roomArtifacts.size() > 0;
    return has;
  }

  public int indexOfRoomArtifact(Artifact aRoomArtifact)
  {
    int index = roomArtifacts.indexOf(aRoomArtifact);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRoomArtifacts()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addRoomArtifact(Artifact aRoomArtifact)
  {
    boolean wasAdded = false;
    if (roomArtifacts.contains(aRoomArtifact)) { return false; }
    roomArtifacts.add(aRoomArtifact);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRoomArtifact(Artifact aRoomArtifact)
  {
    boolean wasRemoved = false;
    if (roomArtifacts.contains(aRoomArtifact))
    {
      roomArtifacts.remove(aRoomArtifact);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRoomArtifactAt(Artifact aRoomArtifact, int index)
  {  
    boolean wasAdded = false;
    if(addRoomArtifact(aRoomArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRoomArtifacts()) { index = numberOfRoomArtifacts() - 1; }
      roomArtifacts.remove(aRoomArtifact);
      roomArtifacts.add(index, aRoomArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRoomArtifactAt(Artifact aRoomArtifact, int index)
  {
    boolean wasAdded = false;
    if(roomArtifacts.contains(aRoomArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRoomArtifacts()) { index = numberOfRoomArtifacts() - 1; }
      roomArtifacts.remove(aRoomArtifact);
      roomArtifacts.add(index, aRoomArtifact);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRoomArtifactAt(aRoomArtifact, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    roomArtifacts.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "roomID" + ":" + getRoomID()+ "," +
            "name" + ":" + getName()+ "," +
            "capacity" + ":" + getCapacity()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "artifacts" + "=" + (getArtifacts() != null ? !getArtifacts().equals(this)  ? getArtifacts().toString().replaceAll("  ","    ") : "this" : "null");
  }
}