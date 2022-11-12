package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Loan;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepo;

    @Autowired
    ArtifactRepository artifactRepo;

    @Transactional
    public Loan getLoanByID(int id) {
        Loan loan = loanRepo.findLoanRequestByRequestID(id);
        if (loan == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        return loan;
    }

    @Transactional
    public Loan createLoan(Loan loan) {
        if (loanRepo.findLoanRequestByRequestID((loan.getRequestID())) != null) {
            throw new DatabaseException(HttpStatus.CONFLICT, "This loan already exists");
        }
        loan = loanRepo.save(loan);
        return loan;
    }

    @Transactional
    public Loan createLoan(List<Integer> artifactIDList) {
        if (artifactIDList.size() == 0 || artifactIDList.size() > 5) {
            throw new RuntimeException("Each loan can only contain maximum of 5 artifacts");
        }
        Loan loan = new Loan();
        int loanFee = 0;
        loan.setApproved(false);
        loan.setNewrequestedArtifactsList();
        for (int artifactID: artifactIDList) {
            if (!artifactRepo.existsById(artifactID)) {
                throw new DatabaseException(HttpStatus.NOT_FOUND, "Artifact for Loan is not in database");
            }
            Artifact artifact = artifactRepo.findByArtID(artifactID);
            loan.addRequestedArtifact(artifact);
            loanFee += artifact.getLoanFee();
        }
        loan.setLoanFee(loanFee);
        loan = loanRepo.save(loan);
        return loan;
    }

    @Transactional
    public boolean setLoanApprovalToTrue(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
        boolean currentLoanStatus = loan.getApproved();
        // if the loan status is already true, this operation should not be allowed
        if (currentLoanStatus) {
            return false;
        }
        loan.setApproved(true);
        loanRepo.save(loan);
        return true;
    }

//    @Transactional
//    public boolean setLoanFee(int loanID, int newLoanFee) {
//        if (!loanRepo.existsById(loanID)) {
//            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
//        }
//        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
//        int currentLoanFee = loan.getLoanFee();
//        if (currentLoanFee == newLoanFee) {
//            return false;
//        }
//        loan.setLoanFee(newLoanFee);
//        loanRepo.save(loan);
//        return true;
//    }

}
