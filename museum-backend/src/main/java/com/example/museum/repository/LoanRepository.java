package com.example.museum.repository;


import com.example.museum.model.Loan;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Integer> {
    Loan findLoanRequestByRequestID(int id);
}
