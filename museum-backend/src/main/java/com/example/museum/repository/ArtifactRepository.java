package com.example.museum.repository;

import com.example.museum.model.Artifact;
import org.springframework.data.repository.CrudRepository;

// extending the repository
public interface ArtifactRepository extends CrudRepository<Artifact, Integer> {
    // DAO implementation here
    // using artID to find Artifact object
    Artifact findByArtID(int id);
}