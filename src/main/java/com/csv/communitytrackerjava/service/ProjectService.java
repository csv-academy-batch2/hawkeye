package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.*;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;


public interface ProjectService {
    ProjectResponseDTO saveProject(ProjectAddDTO projectAddDTO) throws ProjectCodeExistException;

    ProjectResponseDTO updateProject(ProjectUpdateDTO projectUpdateDTO, Integer id) throws Exception;

    Page<ProjectDTO> findAllProject(Pageable pageable);


    Page<ProjectGetPeopleDTO> findPeopleByProjectId(Pageable pageable, Set<Integer> id) throws Exception;
    
    ProjectResponseDTO deleteProject(int id) throws Exception;

    ProjectResponseDTO findProjectById(int id) throws Exception;

}
