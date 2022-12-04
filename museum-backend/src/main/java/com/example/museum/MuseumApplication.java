package com.example.museum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Business;
import com.example.museum.model.Customer;
import com.example.museum.model.Employee;
import com.example.museum.model.Owner;
import com.example.museum.model.Room;
import com.example.museum.service.MuseumSetupService;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
public class MuseumApplication {
	@Autowired
	MuseumSetupService museumSetupService;

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		try {
			Owner createdOwner = museumSetupService.createOwner();
			List<Room> createdRooms = museumSetupService.createRooms();
			Business createdBusiness = museumSetupService.createBusiness();
			Customer createdCustomer = museumSetupService.createCustomer();
			Employee createdEmployee = museumSetupService.createEmployee();
		} catch (DatabaseException e) {
			System.out.println("Error");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(MuseumApplication.class, args);
	}

}
