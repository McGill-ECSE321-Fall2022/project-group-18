package com.example.museum.dto;

import com.example.museum.model.BusinessHour;

import java.sql.Date;
import java.sql.Time;

public class BusinessHourDto {

    private int businessHourID;
    private Date day;
    private Time openTime;
    private Time closeTime;

    public BusinessHourDto(BusinessHour businessHour){
        this.businessHourID = businessHour.getBusinessHourID();
        this.day = businessHour.getDay();
        this.openTime = businessHour.getOpenTime();
        this.closeTime = businessHour.getCloseTime();
    }

    public BusinessHourDto(){}

    public int getBusinessHourID(){
        return businessHourID;
    }

    public Date getDay(){
        return day;
    }

    public Time getOpenTime(){
        return openTime;
    }

    public Time getCloseTime(){
        return closeTime;
    }

    public BusinessHour toModel(){
        BusinessHour businessHour = new BusinessHour();
        businessHour.setBusinessHourID(this.businessHourID);
        businessHour.setDay(this.getDay());
        businessHour.setOpenTime(this.getOpenTime());
        businessHour.setCloseTime(this.getCloseTime());
        return businessHour;
    }
}
