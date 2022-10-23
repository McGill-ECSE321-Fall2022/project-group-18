package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// line 6 "model.ump"
// line 94 "model.ump"
@Entity
public class Artifact extends ArtifactAbs
{
  public Artifact() {}

  //------------------------
  // ENUMERATIONS
  //------------------------


  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Artifact Attributes


  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Artifact(String aName, ArtType aType, int aArtID, boolean aLoanable)
  {
    super(aName, aType, aArtID, aLoanable);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {}

}