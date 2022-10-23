package com.example.museum.repository;

import com.example.museum.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PersonAbsRepository<T extends Person> extends CrudRepository<T, Integer> {
    T findByAccountID(int id);
}
