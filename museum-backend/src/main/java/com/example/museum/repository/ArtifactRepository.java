package com.example.museum.repository;

import com.example.museum.model.Artifact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArtifactRepository extends ArtifactAbsRepository<Artifact>,  CrudRepository<Artifact, Integer> {
}