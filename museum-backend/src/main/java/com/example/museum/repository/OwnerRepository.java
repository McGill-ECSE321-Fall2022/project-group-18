package com.example.museum.repository;

import com.example.museum.model.Owner;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

// extending the Person Repository to read the attributes of Person
// extending the Crud Repository to write the Owner
@Transactional
public interface OwnerRepository extends PersonRepository<Owner>, CrudRepository<Owner, Integer> {
}
