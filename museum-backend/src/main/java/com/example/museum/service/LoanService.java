package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Loan;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
    public List<Artifact> getArtifactsByLoanID(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
        return loan.getRequestedArtifacts();
    }

    @Transactional
    public List<Integer> getArtifactsIDByLoanID(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
        List<Integer> artifactsIDList = new ArrayList<>();
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifactsIDList.add(artifact.getArtID());
        }
        return artifactsIDList;
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
            throw new RequestException(HttpStatus.BAD_REQUEST, "Each loan can only contain maximum of 5 artifacts");
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
            if (!artifact.getLoanable()) {
                throw new RequestException(HttpStatus.BAD_REQUEST, "Artifact for Loan must be loanable");
            }
            loan.addRequestedArtifact(artifact);
            loanFee += artifact.getLoanFee();
        }
        loan.setLoanFee(loanFee);
        loan = loanRepo.save(loan);
        return loan;
    }

    // set loan to true
    // move the loan out of the room
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

    @Transactional
    public boolean setArtifactsInLoanToLoaned(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifact.setLoaned(true);
            artifact = artifactRepo.save(artifact);
        }
        loan = loanRepo.save(loan);
        return true;
    }

    @Transactional
    public void setArtifactsInLoanToUnloaned(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifact.setLoaned(false);
            artifact = artifactRepo.save(artifact);
        }
        loanRepo.save(loan);
    }

    @Transactional
    public void deleteLoan(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        loanRepo.deleteById(loanID);
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
