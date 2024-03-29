package com.example.museum.model;/*PLEASE DO NOT EDIT THIS CODE*/

/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.sql.Date;
import java.sql.Time;

// line 38 "model.ump"
// line 127 "model.ump"
@Entity
public class Employee extends Person {
  public Employee() {
    employeeHours = new ArrayList<EmployeeHour>();
  }

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Employee Associations
  @OneToMany
  private List<EmployeeHour> employeeHours;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Employee(int aAccountID, String aUsername, String aPassword, String aFirstName, String aLastName) {
    super(aAccountID, aUsername, aPassword, aFirstName, aLastName);
    employeeHours = new ArrayList<EmployeeHour>();
  }

  // ------------------------
  // INTERFACE
  // ------------------------
  /* Code from template association_GetMany */

  public void setEmployeeHourList() {
    this.employeeHours = new ArrayList<EmployeeHour>();
  }

  public EmployeeHour getEmployeeHour(int index) {
    EmployeeHour aEmployeeHour = employeeHours.get(index);
    return aEmployeeHour;
  }

  public List<EmployeeHour> getEmployeeHours() {
    List<EmployeeHour> newEmployeeHours = Collections.unmodifiableList(employeeHours);
    return newEmployeeHours;
  }

  public int numberOfEmployeeHours() {
    int number = employeeHours.size();
    return number;
  }

  public boolean hasEmployeeHours() {
    boolean has = employeeHours.size() > 0;
    return has;
  }

  public int indexOfEmployeeHour(EmployeeHour aEmployeeHour) {
    int index = employeeHours.indexOf(aEmployeeHour);
    return index;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEmployeeHours() {
    return 0;
  }

  /* Code from template association_AddUnidirectionalMany */
  public boolean addEmployeeHour(EmployeeHour aEmployeeHour) {
    boolean wasAdded = false;
    if (employeeHours.contains(aEmployeeHour)) {
      return false;
    }
    employeeHours.add(aEmployeeHour);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEmployeeHour(EmployeeHour aEmployeeHour) {
    boolean wasRemoved = false;
    if (employeeHours.contains(aEmployeeHour)) {
      employeeHours.remove(aEmployeeHour);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addEmployeeHourAt(EmployeeHour aEmployeeHour, int index) {
    boolean wasAdded = false;
    if (addEmployeeHour(aEmployeeHour)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfEmployeeHours()) {
        index = numberOfEmployeeHours() - 1;
      }
      employeeHours.remove(aEmployeeHour);
      employeeHours.add(index, aEmployeeHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEmployeeHourAt(EmployeeHour aEmployeeHour, int index) {
    boolean wasAdded = false;
    if (employeeHours.contains(aEmployeeHour)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfEmployeeHours()) {
        index = numberOfEmployeeHours() - 1;
      }
      employeeHours.remove(aEmployeeHour);
      employeeHours.add(index, aEmployeeHour);
      wasAdded = true;
    } else {
      wasAdded = addEmployeeHourAt(aEmployeeHour, index);
    }
    return wasAdded;
  }

  public void delete() {
    employeeHours.clear();
    super.delete();
  }

}