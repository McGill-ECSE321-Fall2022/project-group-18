package com.example.museum.controller;

import com.example.museum.model.Owner;
import com.example.museum.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    OwnerService ownerService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createOwner(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Owner owner = mapper.readValue(body, Owner.class);
            Owner persistedOwner = ownerService.createOwner(owner);
            return new ResponseEntity<>(persistedOwner, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOwner(@PathVariable(name = "id") int id) {
        return ownerService.retrieveOwner(id)
                .map(owner -> new ResponseEntity<>(owner, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
