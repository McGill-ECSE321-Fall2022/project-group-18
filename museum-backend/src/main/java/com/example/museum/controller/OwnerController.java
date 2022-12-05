package com.example.museum.controller;

import com.example.museum.dto.OwnerDto;
import com.example.museum.model.Owner;
import com.example.museum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class OwnerController {
    @Autowired
    OwnerService ownerService;

    //create an owner by passing an owner dto
    @PostMapping("/owner")
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto request) {
        Owner ownerToCreate = request.toModel();
        Owner createdOwner = ownerService.createOwner(ownerToCreate);
        OwnerDto response = new OwnerDto(createdOwner);
        return new ResponseEntity<OwnerDto>(response, HttpStatus.CREATED);
    }

    //log in an owner by passing an owner dto (check their credentials and see if they are allowed to log in)
    @PostMapping("/owner/login")
    public ResponseEntity<Integer> loginOwner(@RequestBody OwnerDto request) {
        Owner ownerToLogin = request.toModel();
        int id = ownerService.loginOwner(ownerToLogin);
        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

    //get the owner by passing its id (returns an owner dto)
    @GetMapping("/owner/{id}")
    public ResponseEntity<OwnerDto> getOwnerByAccountID(@PathVariable int id) {
        Owner owner = ownerService.getOwnerByID(id);
        return new ResponseEntity<OwnerDto>(new OwnerDto(owner), HttpStatus.OK);
    }

    //update the owner. finds the owner to update with an id and updates it with the inputted owner dto
    @PostMapping("/owner/update/{id}")
    public ResponseEntity<OwnerDto> updatedOwner(@PathVariable int id, @RequestBody OwnerDto request) {
        Owner updatedOwner = ownerService.modifyOwnerByID(id, request.getUsername(), request.getPassword(),
                request.getFirstName(), request.getLastName());
        OwnerDto response = new OwnerDto(updatedOwner);
        return new ResponseEntity<OwnerDto>(response, HttpStatus.OK);
    }
}
