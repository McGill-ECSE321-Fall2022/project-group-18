package com.example.museum.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



// line 86 "model.ump"
// line 165 "model.ump"
public class Room
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Attributes
  private String name;
  private int capacity;
  private Artifact artifacts;

  //Room Associations
  private Artifact artifact;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(String aName, int aCapacity, Artifact aArtifacts)
  {
    name = aName;
    capacity = aCapacity;
    artifacts = aArtifacts;
  }

  //------------------------
  // INTERFACE
  //------------------------

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
            "name" + ":" + getName()+ "," +
            "capacity" + ":" + getCapacity()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "artifacts" + "=" + (getArtifacts() != null ? !getArtifacts().equals(this)  ? getArtifacts().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "artifact = "+(getArtifact()!=null?Integer.toHexString(System.identityHashCode(getArtifact())):"null");
  }
}