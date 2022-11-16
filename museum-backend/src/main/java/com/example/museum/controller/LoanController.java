package com.example.museum.controller;



import com.example.museum.dto.ArtifactDto;
import com.example.museum.dto.LoanDto;
import com.example.museum.model.Artifact;
import com.example.museum.model.Loan;
import com.example.museum.model.Room;
import com.example.museum.service.ArtifactService;
import com.example.museum.service.LoanService;
import com.example.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ArtifactService artifactService;

    @Autowired
    RoomService roomService;

    // return a loan dto containing loan ID, loan fee, approval status, list of artifact
    @GetMapping(value = {"/loan/{loanID}", "/loan/{loanID}/" })
    public ResponseEntity<LoanDto> getLoanByLoanID(@PathVariable int loanID) {
        Loan loan = loanService.getLoanByID(loanID);
        return new ResponseEntity<LoanDto>(new LoanDto(loan), HttpStatus.OK);
    }

    @PostMapping(value = {"/loan", "/loan/"})
    public ResponseEntity<LoanDto> createLoan(@RequestParam List<Integer> artifactIDList) {

        Loan loan = loanService.createLoan(artifactIDList);
        LoanDto response = new LoanDto(loan);
        return new ResponseEntity<LoanDto>(response, HttpStatus.OK);
    }

    @PostMapping(value = {"/loan/update/approve", "/loan/update/approve/"})
    public ResponseEntity<LoanDto> updateLoanApproval(@RequestParam int loanID) {
        loanService.setLoanApprovalToTrue(loanID); // set loan status to true, referring to an active loan
        loanService.setArtifactsInLoanToLoaned(loanID); // set status of all artifacts belonged to true
        List<Artifact> artifactList = loanService.getLoanByID(loanID).getRequestedArtifacts();
        // remove all artifacts from rooms
        List<Room> roomList = roomService.removeArtifactsFromRooms(artifactList);
        Loan loan = loanService.getLoanByID(loanID);
        return new ResponseEntity<LoanDto>(new LoanDto(loan), HttpStatus.OK);
    }

    // remove a loan when 1. employee reject the loan
    @PostMapping(value = {"/loan/update/remove", "/loan/update/remove/"})
    public ResponseEntity<List<ArtifactDto>> updateLoanRemove(@RequestParam int loanID) {
        // if rejected, simply delete the loan object, since artifacts are still in the room
        List<ArtifactDto> artifactDtoList = new ArrayList<>();
        Loan loan = loanService.getLoanByID(loanID);
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifactDtoList.add(new ArtifactDto(artifact));
        }

        loanService.deleteLoan(loanID);

        return new ResponseEntity<List<ArtifactDto>>(artifactDtoList, HttpStatus.OK);
    }

    @PostMapping(value = {"/loan/update/return", "/loan/update/return/"})
    public ResponseEntity<List<ArtifactDto>> updateLoanReturn(@RequestParam int loanID) {
        // when a loan is returned, 1. artifacts set loaned status to false
        //                          2. artifacts go to storage room
        //                          3. loan object is deleted
        List<ArtifactDto> artifactDtoList = new ArrayList<>();
        Loan loan = loanService.getLoanByID(loanID);
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifactDtoList.add(new ArtifactDto(artifact));
        }
        List<Integer> artifactIDList = loanService.getArtifactsIDByLoanID(loanID);
        loanService.setArtifactsInLoanToUnloaned(loanID);
        roomService.addArtifactsToRoom("Storage", artifactIDList);
        loanService.deleteLoan(loanID);
        return new ResponseEntity<List<ArtifactDto>>(artifactDtoList, HttpStatus.OK);
    }

}
