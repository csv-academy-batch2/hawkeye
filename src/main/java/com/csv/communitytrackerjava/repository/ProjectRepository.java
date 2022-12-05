package com.csv.communitytrackerjava.repository;

import com.csv.communitytrackerjava.model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepository extends PagingAndSortingRepository<Project, Integer> {
    Optional<Project> findByProjectCode(String projectCode);

    List<Project> findAllByProjectIdIn(Set<Integer> id, Pageable pageable);
    List<Project> findByIsActiveTrue(Pageable pageable);

}
