package com.example.museum.repository;

import com.example.museum.model.Owner;

import javax.transaction.Transactional;

@Transactional
public interface OwnerRepository extends PersonAbsRepository<Owner> {
}
