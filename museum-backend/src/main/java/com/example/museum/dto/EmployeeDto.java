package com.example.museum.dto;

import com.example.museum.model.Employee;
import com.example.museum.model.EmployeeHour;

public class EmployeeDto {
    private int accountID;
    private String username;
    private String password;
    private List<EmployeeHourDto> employeeHours;

    public EmployeeDto(Employee employee) {
        this.accountID = employee.getAccountID();
        this.username = employee.getUsername();
        this.password = employee.getPassword();

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
