package com.csv.communitytrackerjava.repository;

import com.csv.communitytrackerjava.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByProjectCode(String projectCode);
    Optional<Project> findByIsActive(Boolean isActive);
}
