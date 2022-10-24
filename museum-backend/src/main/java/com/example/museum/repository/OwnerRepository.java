package com.example.museum.repository;

import com.example.museum.model.Owner;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface OwnerRepository extends PersonRepository<Owner>, CrudRepository<Owner, Integer> {
}
