package com.example.museum.dto;

import com.example.museum.model.Business;

import java.util.List;

public class BusinessDto {
    private int businessID;
    private int ticketFee;
    private List<BusinessHourDto> businessHours;

    public BusinessDto(Business business){
        this.businessID = businessID;
        this.ticketFee = ticketFee;
        this.businessHours = businessHours;
    }

    public int getBusinessID() {
        return businessID;
    }

    public int getTicketFee() {
        return ticketFee;
    }

    public List<BusinessHourDto> getBusinessHours() {
        return businessHours;
    }
}
