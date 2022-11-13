package com.example.museum.dto;

import java.util.List;

import com.example.museum.model.Employee;
import com.example.museum.model.EmployeeHour;

public class EmployeeDto {
    private int accountID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private List<EmployeeHourDto> employeeHours;

    public EmployeeDto(Employee employee) {
        this.accountID = employee.getAccountID();
        this.username = employee.getUsername();
        this.password = employee.getPassword();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        for (EmployeeHour eh : employee.getEmployeeHours()) {
            this.employeeHours.add(new EmployeeHourDto(eh));
        }
    }

    public EmployeeDto() {
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

    public List<EmployeeHourDto> getEmployeeHours() {
        return this.employeeHours;
    }

    public Employee toModel() {
        Employee employee = new Employee();
        employee.setAccountID(this.accountID);
        employee.setUsername(this.username);
        employee.setPassword(this.password);
        for (EmployeeHourDto eh : this.getEmployeeHours()) {
            employee.addEmployeeHour(eh.toModel());
        }

        return employee;
    }

}
