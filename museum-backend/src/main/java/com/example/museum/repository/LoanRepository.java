package com.example.museum.repository;


import com.example.museum.model.Loan;
import org.springframework.data.repository.CrudRepository;

// extending the repository
public interface LoanRepository extends CrudRepository<Loan, Integer> {
    // DAO implementation here
    // using requestID to find Loan object
    Loan findLoanRequestByRequestID(int id);
}
