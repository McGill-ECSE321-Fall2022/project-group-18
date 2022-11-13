package com.example.museum.controller;



import com.example.museum.dto.LoanDto;
import com.example.museum.model.Loan;
import com.example.museum.service.ArtifactService;
import com.example.museum.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ArtifactService artifactService;

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

    @PostMapping("/loan/update")
    public ResponseEntity<LoanDto> updateLoanApproval(@RequestParam int loanID) {
        loanService.setLoanApprovalToTrue(loanID);
        Loan loan = loanService.getLoanByID(loanID);
        return new ResponseEntity<LoanDto>(new LoanDto(loan), HttpStatus.OK);
    }
}
