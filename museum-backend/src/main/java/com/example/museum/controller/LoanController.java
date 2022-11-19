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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getLoanByLoanID(@PathVariable int loanID) {
        Loan loan = loanService.getLoanByID(loanID);
        Map<String, Object> response = new HashMap<>();
        response.put("loanID", loan.getRequestID());
        response.put("loanFee", loan.getLoanFee());
        response.put("loanStatus", loan.getApproved());
        List<Integer> artifactIDList = new ArrayList<>();
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifactIDList.add(artifact.getArtID());
        }
        response.put("loanArtifactIDList", artifactIDList);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/loan", "/loan/"})
    public ResponseEntity<Integer> createLoan(@RequestParam List<Integer> artifactIDList) {

        Loan loan = loanService.createLoan(artifactIDList);
//        LoanDto response = new LoanDto();
//        response.setLoanFee(loan.getLoanFee());
//        response.setLoanApproval(loan.getApproved());
//        response.setLoanID(loan.getRequestID());
//        List<Integer> receivedArtifactIDList = new ArrayList<>();
//        for (Artifact artifact: loan.getRequestedArtifacts()) {
//            receivedArtifactIDList.add(artifact.getArtID());
//        }
//        response.setArtifactDtoList(receivedArtifactIDList);
        return new ResponseEntity<Integer>(loan.getRequestID(), HttpStatus.OK);
    }

    @GetMapping(value = {"/loan/update/approve", "/loan/update/approve/"})
    public ResponseEntity<Map<String, Object>> updateLoanApproval(@RequestParam int loanID) {
        loanService.setLoanApprovalToTrue(loanID); // set loan status to true, referring to an active loan
        loanService.setArtifactsInLoanToLoaned(loanID); // set status of all artifacts belonged to true
        List<Artifact> artifactList = loanService.getLoanByID(loanID).getRequestedArtifacts();
        // remove all artifacts from rooms
        List<Room> roomList = roomService.removeArtifactsFromRooms(artifactList);
        List<Integer> roomIDList = new ArrayList<>();
        for (Room room: roomList) {
            roomIDList.add(room.getRoomID());
        }

        Loan loan = loanService.getLoanByID(loanID);
        Map<String, Object> response = new HashMap<>();
        response.put("loanID", loan.getRequestID());
        response.put("loanFee", loan.getLoanFee());
        response.put("loanStatus", loan.getApproved());
        List<Integer> artifactIDList = new ArrayList<>();
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifactIDList.add(artifact.getArtID());
        }
        response.put("loanArtifactIDList", artifactIDList);
        response.put("loanArtifactRoomIDList", roomIDList);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    // remove a loan when 1. employee reject the loan
    @GetMapping(value = {"/loan/update/remove", "/loan/update/remove/"})
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

    @GetMapping(value = {"/loan/update/return", "/loan/update/return/"})
    public ResponseEntity<List<Integer>> updateLoanReturn(@RequestParam int loanID) {
        // when a loan is returned, 1. artifacts set loaned status to false
        //                          2. artifacts go to storage room
        //                          3. loan object is deleted

        List<Integer> artifactIDList = loanService.getArtifactsIDByLoanID(loanID);
        loanService.setArtifactsInLoanToUnloaned(loanID);
        roomService.addArtifactsToRoom("Storage", artifactIDList);
        loanService.deleteLoan(loanID);

        return new ResponseEntity<List<Integer>>(artifactIDList, HttpStatus.OK);
    }

}
