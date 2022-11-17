package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Employee;
import com.example.museum.model.EmployeeHour;
import com.example.museum.repository.EmployeeHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class EmployeeHourService {

    @Autowired
    EmployeeHourRepository employeeHourRepository;

    @Transactional
    public EmployeeHour createEmployeeHour(EmployeeHour employeeHour) {
        if (employeeHourRepository.findEmployeeHourByEmployeeHourID(employeeHour.getEmployeeHourID()) != null) {
            throw new DatabaseException(HttpStatus.CONFLICT, "A emloyee hour with the given id already exists.");
        }
        Iterator<EmployeeHour> eHours = employeeHourRepository.findAll().iterator();
        while(eHours.hasNext()){
            EmployeeHour eH = eHours.next();
            if(eH.getDay().toString().equals(employeeHour.getDay().toString())){
                throw new DatabaseException(HttpStatus.CONFLICT, "A EmployeeHour with the given date already exists");
            }
        }

        employeeHour = employeeHourRepository.save(employeeHour);
        return employeeHour;
    }

    @Transactional
    public List<EmployeeHour> getAllEmployeeHours() {
        List<EmployeeHour> employeeHours = new ArrayList<>();
        Iterator<EmployeeHour> eHours = employeeHourRepository.findAll().iterator();
        while (eHours.hasNext()) {
            EmployeeHour eh = eHours.next();
            employeeHours.add(eh);
        }
        return employeeHours;
    }


    @Transactional
    public EmployeeHour getEmployeeHourById(int id){
        EmployeeHour employeeHour = employeeHourRepository.findEmployeeHourByEmployeeHourID(id);
        if(employeeHour == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "EmployeeHour not found");
        }
        return employeeHour;
    }
    
    public EmployeeHour modifyEmployeeHourById(int id, Date day, Time startTime, Time endTime){
        Iterator<EmployeeHour> eHours = employeeHourRepository.findAll().iterator();
        while(eHours.hasNext()){
            EmployeeHour eH = eHours.next();
            if(eH.getDay().toString().equals(day.toString())){
                throw new DatabaseException(HttpStatus.CONFLICT, "A EmployeeHour with the given date already exists");
            }
        }
        EmployeeHour employeeHour = employeeHourRepository.findEmployeeHourByEmployeeHourID(id);
        employeeHour.setDay(day);
        employeeHour.setStartTime(startTime);
        employeeHour.setEndTime(endTime);
        
        EmployeeHour updatedEmployeeHour = employeeHourRepository.save(employeeHour);
        return updatedEmployeeHour;
    }
}