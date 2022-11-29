package com.csv.communitytrackerjava.repository;

import com.csv.communitytrackerjava.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends PagingAndSortingRepository<Project, Integer> {
    Optional<Project> findByProjectCode(String projectCode);

    Page<List<Project>> findByProjectId(Integer id, Pageable pageable);
    
    Page<List<Project>> findByProjectDesc(String projectDesc, Pageable pageable);

}
