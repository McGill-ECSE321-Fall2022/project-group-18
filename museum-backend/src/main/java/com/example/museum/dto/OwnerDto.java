package com.example.museum.dto;

import com.example.museum.model.Owner;

public class OwnerDto {
    private int accountID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private BusinessDto business;

    public OwnerDto(Owner owner) {
        this.accountID = owner.getAccountID();
        this.username = owner.getUsername();
        this.password = owner.getPassword();
        this.firstName = owner.getFirstName();
        this.lastName = owner.getLastName();
        this.business = new BusinessDto(owner.getBusiness());
    }

    public OwnerDto() {
    }

    public int getAccountID() {
        return this.accountID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public BusinessDto getBusiness() {
        return this.business;
    }

    public Owner toModel() {
        Owner owner = new Owner();
        owner.setAccountID(this.getAccountID());
        owner.setUsername(this.getUsername());
        owner.setPassword(this.getPassword());
        owner.setFirstName(this.getFirstName());
        owner.setLastName(this.getLastName());
        owner.setBusiness(this.business.toModel());

        return owner;
    }
}
