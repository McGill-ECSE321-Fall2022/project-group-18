package com.example.museum.dto;

import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;

import java.util.List;

public class BusinessDto {
    private int businessID;
    private int ticketFee;
    private List<BusinessHour> businessHours;

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

    public List<BusinessHour> getBusinessHours() {
        return businessHours;
    }
}
