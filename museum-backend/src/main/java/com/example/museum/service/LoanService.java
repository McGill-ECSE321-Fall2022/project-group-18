package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Customer;
import com.example.museum.model.Loan;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.CustomerRepository;
import com.example.museum.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepo;

    @Autowired
    ArtifactRepository artifactRepo;

    @Autowired
    CustomerRepository customerRepo;

    // get Loan object from loan id
    @Transactional
    public Loan getLoanByID(int id) {
        if (!loanRepo.existsById(id)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(id);
        return loan;
    }


    // get all artifact in the loan by loan id
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

    // get a map<loansID, customerID> for all loans belonged to customers
    @Transactional
    public Map<String, Integer> getAllCustomerLoans() {
        Map<String, Integer> allCustomerLoansMap = new HashMap<>();
        List<Customer> allCustomerList = (List<Customer>) customerRepo.findAll();
        for (Customer savedCustomer: allCustomerList) {
            for (Loan savedLoan: savedCustomer.getLoans()) {
                allCustomerLoansMap.put(Integer.toString(savedLoan.getRequestID()), savedCustomer.getAccountID());
            }
        }
        return allCustomerLoansMap;
    }


    // create a loan from a list of artifact id
    @Transactional
    public Loan createLoan(List<Integer> artifactIDList) {
        // ensure a loan has no more than 5 artifacts
        if (artifactIDList.size() == 0 || artifactIDList.size() > 5) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Each loan can only contain maximum of 5 artifacts");
        }
        Loan loan = new Loan();
        int loanFee = 0;
        loan.setApproved(false); // default approval status false
        loan.setNewrequestedArtifactsList();
        for (int artifactID: artifactIDList) {
            if (!artifactRepo.existsById(artifactID)) { // artifact should be created
                throw new DatabaseException(HttpStatus.NOT_FOUND, "Artifact for Loan is not in database");
            }
            Artifact artifact = artifactRepo.findByArtID(artifactID);
            if (!artifact.getLoanable() || artifact.getLoanFee() < 0) {
                throw new RequestException(HttpStatus.BAD_REQUEST, "Artifact for Loan must be loanable with a valid fee");
            }
            if (artifact.getLoaned()) {
                throw new RequestException(HttpStatus.BAD_REQUEST, "Artifact for Loan must not be loaned");
            }
            loan.addRequestedArtifact(artifact);
            loanFee += artifact.getLoanFee(); // loan fee is automatically calculated
        }
        loan.setLoanFee(loanFee);
        loan = loanRepo.save(loan);
        return loan;
    }

    // set loan to true and move the loan's artifact out of their room
    @Transactional
    public Loan setLoanApprovalToTrue(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
        loan.setApproved(true);
        loan = loanRepo.save(loan);
        return loan;
    }

    // set artifacts' loaned status to true
    @Transactional
    public Loan setArtifactsInLoanToLoaned(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifact.setLoaned(true);
            artifact = artifactRepo.save(artifact);
        }
        loan = loanRepo.save(loan);
        return loan;
    }

    // set artifacts' loaned status to false
    @Transactional
    public Loan setArtifactsInLoanToUnloaned(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        Loan loan = loanRepo.findLoanRequestByRequestID(loanID);
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            artifact.setLoaned(false);
            artifact = artifactRepo.save(artifact);
        }
        loan = loanRepo.save(loan);
        return loan;
    }

    // delete a loan, must be done after removed from customer's loan list first.
    @Transactional
    public void deleteLoan(int loanID) {
        if (!loanRepo.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        loanRepo.deleteById(loanID);
    }


}
