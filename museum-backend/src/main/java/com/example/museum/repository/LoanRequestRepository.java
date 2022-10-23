package com.example.museum.repository;

import com.example.museum.model.LoanRequest;
import org.springframework.data.repository.CrudRepository;

public interface LoanRequestRepository extends CrudRepository<LoanRequest, Integer> {
    LoanRequest findLoanRequestByRequestID(int id);
}
