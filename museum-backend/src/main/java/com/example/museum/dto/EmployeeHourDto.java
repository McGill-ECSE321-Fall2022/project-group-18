package com.example.museum.dto;

import com.example.museum.model.EmployeeHour;

import java.sql.Date;
import java.sql.Time;

public class EmployeeHourDto {

    private int employeeHourID;
    private Date day;
    private Time startTime;
    private Time endTime;

    public EmployeeHourDto(EmployeeHour employeeHour){
        this.employeeHourID = employeeHour.getEmployeeHourID();
        this.day = employeeHour.getDay();
        this.startTime = employeeHour.getStartTime();
        this.endTime = employeeHour.getEndTime();
    }

    public EmployeeHourDto(){}

    public int getEmployeeHourID(){
        return employeeHourID;
    }

    public Date getDay(){
        return day;
    }

    public Time getStartTime(){
        return startTime;
    }

    public Time getEndTime(){
        return endTime;
    }

    public EmployeeHour toModel(){
        EmployeeHour employeeHour = new EmployeeHour();
        employeeHour.setEmployeeHourID(this.employeeHourID);
        employeeHour.setDay(this.getDay());
        employeeHour.setStartTime(this.getStartTime());
        employeeHour.setEndTime(this.getEndTime());
        return employeeHour;
    }
}