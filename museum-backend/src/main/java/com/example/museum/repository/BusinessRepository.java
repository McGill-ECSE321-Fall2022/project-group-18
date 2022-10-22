package com.example.museum.repository;

import com.example.museum.model.Business;
import org.springframework.data.repository.CrudRepository;

public interface BusinessRepository extends CrudRepository<Business, Integer> {
    public Business findArtifactByBusinessID(int id);
}
