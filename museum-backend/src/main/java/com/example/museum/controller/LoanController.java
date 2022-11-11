package com.example.museum.controller;



import com.example.museum.dto.LoanDto;
import com.example.museum.model.Loan;
import com.example.museum.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoanController {

    @Autowired
    LoanService loanService;

    @GetMapping("/loan/{loanID}")
    public ResponseEntity<LoanDto> getLoanByLoanID(@PathVariable int loanID) {
        Loan loan = loanService.getLoanByID(loanID);
        return new ResponseEntity<LoanDto>(new LoanDto(loan), HttpStatus.OK);
    }

    @PostMapping("/loan")
    public ResponseEntity<LoanDto> createLoan(@RequestBody LoanDto loanDto) {
        Loan loan = loanDto.toModel();
        loan = loanService.createLoan(loan);
        LoanDto response = new LoanDto(loan);
        return new ResponseEntity<LoanDto>(response, HttpStatus.OK);
    }
}
