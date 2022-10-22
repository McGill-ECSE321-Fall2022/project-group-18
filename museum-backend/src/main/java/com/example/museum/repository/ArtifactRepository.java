package com.example.museum.repository;

import com.example.museum.model.Artifact;
import org.springframework.data.repository.CrudRepository;

public interface ArtifactRepository extends CrudRepository<Artifact, Integer> {
    public Artifact findArtifactByArtID(int id);
}