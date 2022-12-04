package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.*;
import com.example.museum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

@Service
public class MuseumSetupService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BusinessRepository businessRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public List<Room> createRooms(){
        if(roomRepository.findAll().iterator().hasNext()){
            throw new DatabaseException(HttpStatus.CONFLICT, "Rooms already exist in the database");
        }
        List<Room> roomList = new ArrayList<>();

        //small rooms
        for(int i = 1; i <= 5; i ++){
            Room room = new Room();
            room.setCapacity(200);
            room.setNewRoomArtifactsList();
            room.setName("SR" + i);
            Room createdRoom = roomRepository.save(room);
            roomList.add(createdRoom);
        }

        //large rooms
        for(int i = 1; i <= 5; i++){
            Room room = new Room();
            room.setCapacity(300);
            room.setNewRoomArtifactsList();
            room.setName("LR" + i);
            Room createdRoom = roomRepository.save(room);
            roomList.add(createdRoom);
        }

        //storage
        Room storage = new Room();
        storage.setCapacity(MAX_VALUE);
        storage.setNewRoomArtifactsList();
        storage.setName("Storage");
        Room createdStorage = roomRepository.save(storage);
        roomList.add(createdStorage);

        return roomList;
    }

    public Owner createOwner(){
        Owner owner = new Owner();
        owner.setUsername("owner321");
        owner.setPassword("ecse321");
        owner.setFirstName("Marwan");
        owner.setLastName("Kanaan");

        if(ownerRepository.findAll().iterator().hasNext()){
            throw new DatabaseException(HttpStatus.CONFLICT, "Owner already exists in the database");
        }
        Owner returnedOwner = ownerRepository.save(owner);
        return returnedOwner;
    }

    //create a customer in the museum system
    public Customer createCustomer(){
        Customer customer = new Customer();
        customer.setUsername("customer321");
        customer.setPassword("321");
        customer.setFirstName("John");
        customer.setLastName("Doe");

        //the following code is not good since we can have multiple customers in the database...
        // to be thorough we should check if the customer already exists in the database by checking its fields, but this is too much work...
//        if(customerRepository.findAll().iterator().hasNext()){
//            throw new DatabaseException(HttpStatus.CONFLICT,"Customer already exists in the database");
//        }

        Customer returnedCustomer = customerRepository.save(customer);
        return returnedCustomer;
    }

    //create an employee in the museum system
    public Employee createEmployee(){
        Employee employee = new Employee();
        employee.setUsername("employee321");
        employee.setPassword("321");
        employee.setFirstName("Jim");
        employee.setLastName("Halpert");

        Employee returnedEmployee = employeeRepository.save(employee);
        return returnedEmployee;
    }

    //creates a business with a ticket fee of 10
    public Business createBusiness(){
        Business business = new Business();
        business.setTicketFee(10);

        if(businessRepository.findAll().iterator().hasNext()){
            throw new DatabaseException(HttpStatus.CONFLICT, "Business already exists in the database");
        }
        Business returnedBusiness = businessRepository.save(business);
        return returnedBusiness;
    }
}
