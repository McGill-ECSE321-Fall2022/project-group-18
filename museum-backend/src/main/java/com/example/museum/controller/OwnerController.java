package com.example.museum.controller;

import com.example.museum.dto.OwnerDto;
import com.example.museum.model.Owner;
import com.example.museum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OwnerController {
    @Autowired
    OwnerService ownerService;

    @PostMapping("/owner")
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto request) {
        Owner ownerToCreate = request.toModel();
        Owner createdOwner = ownerService.createOwner(ownerToCreate);
        OwnerDto response = new OwnerDto(createdOwner);
        return new ResponseEntity<OwnerDto>(response, HttpStatus.CREATED);
    }

    @PostMapping("/owner/login")
    public ResponseEntity<String> loginOwner(@RequestBody OwnerDto request) {
        Owner ownerToLogin = request.toModel();
        ownerService.loginOwner(ownerToLogin);
        return new ResponseEntity<String>("Successful login", HttpStatus.OK);
    }

    @GetMapping("/owner/{id}")
    public ResponseEntity<OwnerDto> getOwnerByBusinessID(@PathVariable int id) {
        Owner owner = ownerService.getOwnerByID(id);
        return new ResponseEntity<OwnerDto>(new OwnerDto(owner), HttpStatus.OK);
    }

    @PostMapping("/owner/{id}")
    public ResponseEntity<OwnerDto> updatedOwner(@PathVariable int id, @RequestBody OwnerDto request) {
        Owner updatedOwner = ownerService.modifyOwnerByID(id, request.getUsername(), request.getPassword(),
                request.getFirstName(), request.getLastName());
        OwnerDto response = new OwnerDto(updatedOwner);
        return new ResponseEntity<OwnerDto>(response, HttpStatus.OK);
    }
}
