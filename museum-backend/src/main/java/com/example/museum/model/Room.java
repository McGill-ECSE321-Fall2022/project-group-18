package main.java.com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



// line 93 "model.ump"
// line 173 "model.ump"
public class Room
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Attributes
  private int roomID;
  private String name;
  private int capacity;
  private Artifact artifacts;

  //Room Associations
  private Artifact artifact;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(int aRoomID, String aName, int aCapacity, Artifact aArtifacts)
  {
    roomID = aRoomID;
    name = aName;
    capacity = aCapacity;
    artifacts = aArtifacts;
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
  /* Code from template association_GetOne */
  public Artifact getArtifact()
  {
    return artifact;
  }

  public boolean hasArtifact()
  {
    boolean has = artifact != null;
    return has;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setArtifact(Artifact aNewArtifact)
  {
    boolean wasSet = false;
    artifact = aNewArtifact;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    artifact = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "roomID" + ":" + getRoomID()+ "," +
            "name" + ":" + getName()+ "," +
            "capacity" + ":" + getCapacity()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "artifacts" + "=" + (getArtifacts() != null ? !getArtifacts().equals(this)  ? getArtifacts().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "artifact = "+(getArtifact()!=null?Integer.toHexString(System.identityHashCode(getArtifact())):"null");
  }
}