package com.example.museum.repository;

import com.example.museum.model.ArtifactAbs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ArtifactAbsRepository<T extends ArtifactAbs> extends CrudRepository<T, Integer> {
    T findByArtID(int id);
}
