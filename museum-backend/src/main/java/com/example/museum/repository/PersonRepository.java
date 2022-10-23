package com.example.museum.repository;

import com.example.museum.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    public Person findPersonByAccountID(int id);
}
