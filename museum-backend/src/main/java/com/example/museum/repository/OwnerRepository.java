package com.example.museum.repository;

import com.example.museum.model.Owner;
import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends CrudRepository<Owner, Integer> {
    public Owner findOwnerByAccountID(int id);
}
