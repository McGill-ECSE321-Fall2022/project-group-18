package com.example.museum.dto;

import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;

import java.util.ArrayList;
import java.util.List;

public class BusinessDto {
    private int businessID;
    private int ticketFee;
    private List<BusinessHourDto> businessHours;

    public BusinessDto(Business business){
        this.businessID = business.getBusinessID();
        this.ticketFee = business.getTicketFee();
        businessHours = new ArrayList<>();
        for(BusinessHour bh : business.getBusinessHours()){
            this.businessHours.add(new BusinessHourDto(bh));
        }
    }

    public BusinessDto(){}

    public int getBusinessID() {
        return businessID;
    }

    public int getTicketFee() {
        return ticketFee;
    }

    public List<BusinessHourDto> getBusinessHours() {
        return businessHours;
    }

    public Business toModel(){
        Business business = new Business();
        business.setBusinessID(this.getBusinessID());
        business.setTicketFee(this.getTicketFee());
        for(BusinessHourDto bh : this.getBusinessHours()){
            business.addBusinessHour(bh.toModel());
        }
        return business;
    }
}
