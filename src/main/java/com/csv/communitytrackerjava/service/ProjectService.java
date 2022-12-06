package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.ProjectAddDTO;
import com.csv.communitytrackerjava.dto.ProjectGetPeopleDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.dto.ProjectUpdateDTO;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;


public interface ProjectService {
    ProjectResponseDTO saveProject(ProjectAddDTO projectAddDTO) throws ProjectCodeExistException;

    ProjectResponseDTO updateProject(ProjectUpdateDTO projectUpdateDTO, Integer id) throws Exception;

    ProjectResponseDTO findAllProject();

    Page<ProjectGetPeopleDTO> findPeopleByProjectId(Pageable pageable, Set<Integer> id, Boolean includeAll) throws Exception;
    
    ProjectResponseDTO deleteProject(int id) throws Exception;

    ProjectResponseDTO findProjectById(int id) throws Exception;

}
