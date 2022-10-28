package com.example.museum.repository;

import com.example.museum.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

// the persistence layer of an abstract class
// the methods defined here can be used by Customer, Owner and Employee
// the methods can be inherited by classes inherited from Person
@NoRepositoryBean
public interface PersonRepository<T extends Person> extends CrudRepository<T, Integer> {
    T findByAccountID(int id);
}
