package com.example.museum.dto;

import com.example.museum.model.Owner;

public class OwnerDto {
    private int accountID;
    private String username;
    private String password;
    private BusinessDto business;

    public OwnerDto(Owner owner) {
        this.accountID = owner.getAccountID();
        this.username = owner.getUsername();
        this.password = owner.getPassword();
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

    public BusinessDto getBusiness() {
        return this.business;
    }

    public Owner toModel() {
        Owner owner = new Owner();
        owner.setAccountID(this.accountID);
        owner.setUsername(this.username);
        owner.setPassword(this.password);
        owner.setBusiness(this.business.toModel());

        return owner;
    }
}
