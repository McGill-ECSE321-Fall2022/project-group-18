package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Employee;
import com.example.museum.model.EmployeeHour;
import com.example.museum.repository.EmployeeHourRepository;
import com.example.museum.repository.EmployeeRepository;

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
        //checkDateConflict(employeeHour.getEmployeeHourID(), employeeHour.getDay());
        employeeHour = employeeHourRepository.save(employeeHour);
        return employeeHour;
    }

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

    @Transactional
    public EmployeeHour modifyEmployeeHourById(int id, Date day, Time startTime, Time endTime){
        //checkDateConflict(id, day);
        EmployeeHour employeeHour = employeeHourRepository.findEmployeeHourByEmployeeHourID(id);
        if(employeeHour == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "EmployeeHour not found");
        }
        employeeHour.setDay(day);
        employeeHour.setStartTime(startTime);
        employeeHour.setEndTime(endTime);
        EmployeeHour updatedEmployeeHour = employeeHourRepository.save(employeeHour);
        return updatedEmployeeHour;
    }

    //this might be glitched - we need to check that employee hours don't conflict for the same employee - not that they don't conflict at all...
   /* //TODO: shaheer needs to fix his part
    private void checkDateConflict(int id, Date day) throws DatabaseException{
        List<Employee> employees = new ArrayList<>();
        Iterator<Employee> emp = employees.iterator();
        Iterator<EmployeeHour> eHours = employeeHourRepository.findAll().iterator();
        while(emp.hasNext()){
            Employee employee = emp.next();
            while (eHours.hasNext()) {
                EmployeeHour eh = eHours.next();
                if (employee.g   eh.getDay().toString().equals(day.toString()) && (eh.getEmployeeHourID() != id || id == 0)) {
                    throw new DatabaseException(HttpStatus.CONFLICT, "A EmployeeHour with the given date already exists");
                }
            }
    }
}*/ 
}