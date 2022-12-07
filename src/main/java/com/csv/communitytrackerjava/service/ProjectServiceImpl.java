package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.*;
import com.csv.communitytrackerjava.dto.ProjectAddDTO;
import com.csv.communitytrackerjava.dto.ProjectPayloadDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.dto.ProjectUpdateDTO;
import com.csv.communitytrackerjava.exception.InactiveDataException;
import com.csv.communitytrackerjava.exception.InvalidInputException;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import com.csv.communitytrackerjava.exception.RecordNotFoundException;
import com.csv.communitytrackerjava.mapper.ProjectMapper;
import com.csv.communitytrackerjava.model.Project;
import com.csv.communitytrackerjava.repository.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public ProjectResponseDTO saveProject(ProjectAddDTO projectAddDTO) throws ProjectCodeExistException {
        ProjectPayloadDTO payloadDTO = new ProjectPayloadDTO();

        String projectCode = projectAddDTO.getProjectCode();
        Optional<Project> mapCode = projectRepository.findByProjectCode(projectCode);
        if (mapCode.isPresent() && mapCode.get().getIsActive()) {
            throw new ProjectCodeExistException("Project code already exist.");
        }
        String projectDesc = projectAddDTO.getProjectDesc();

        projectAddDTO.setProjectDesc(CaseUtils.toCamelCase(projectDesc, true, ' '));
        Project project = projectMapper.validationToModel(projectAddDTO);
        projectRepository.save(project);
        payloadDTO.setAdditionalProperty("project", projectMapper.toDTO(project));

        return toProjectResponseDTO("Successfully add project.", payloadDTO);
    }

    @Override
    public ProjectResponseDTO updateProject(ProjectUpdateDTO projectUpdateDTO, Integer id) throws Exception {
        ProjectPayloadDTO payloadDTO = new ProjectPayloadDTO();

        Project projectFound = findProject(id);
        Optional<Project> projectCodeCheck = projectRepository
                .findByProjectCode(projectUpdateDTO.getProjectCode());
        projectUpdateDTO.setProjectDesc(StringUtils.isBlank(projectUpdateDTO
                .getProjectDesc()) ? projectFound.getProjectDesc() : CaseUtils
                .toCamelCase(projectUpdateDTO.getProjectDesc(), true, ' '));

        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true);
        if (projectCodeCheck.isPresent() && projectCodeCheck.get().getIsActive()) {
            throw new ProjectCodeExistException("Project code already exist.");
        }

        projectFound.setProjectCode(projectUpdateDTO.getProjectCode());
        modelMapper.map(projectUpdateDTO, projectFound);
        payloadDTO.setAdditionalProperty("projects", projectMapper
                .toDTO(projectRepository.save(projectFound)));
        return toProjectResponseDTO("Successfully update project.", payloadDTO);
    }


    @Override
    public Page<ProjectDTO> findAllProject(Pageable pageable) {
        List<ProjectDTO> projectList = (
                projectRepository.findByIsActiveTrue(pageable)
                        .stream()
                        .map(projectMapper::toDTO)).toList();
        return new PageImpl<>(projectList, pageable, projectList.size());
    }

    @Override
    public Page<ProjectGetPeopleDTO> findPeopleByProjectId(Pageable pageable, Set<Integer> id, Boolean includeAll) throws Exception {
        List<Project> projects = includeAll ? projectRepository.findAllByProjectIdIn(id, pageable) : projectRepository.findAllByProjectIdInAndIsActiveTrue(id, pageable);
        List<ProjectGetPeopleDTO> projectList = (
                projects
                        .stream()
                        .map(projectMapper::toGetPeopleDTO))
                .collect(Collectors.toList());

        if (projectList.isEmpty()) {
            throw new RecordNotFoundException("Project doesn't exist");
        }
        return new PageImpl<>(projectList, pageable, projectList.size());
    }
    
    public ProjectResponseDTO deleteProject(int id) throws Exception {
        ProjectPayloadDTO payloadDTO = new ProjectPayloadDTO();

        Project projectFound = findProject(id);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        projectFound.setIsActive(false);
        return toProjectResponseDTO("Successfully delete project.", payloadDTO);
    }

    @Override
    public ProjectResponseDTO findProjectById(int id) throws Exception {
        ProjectPayloadDTO payloadDTO = new ProjectPayloadDTO();
        Project projectFound = findProject(id);
        payloadDTO.setAdditionalProperty("projects", projectMapper.toDTO(projectFound));
        return toProjectResponseDTO("Successfully fetch", payloadDTO);
    }

    public ProjectResponseDTO toProjectResponseDTO(String message, ProjectPayloadDTO payloadDTO) {
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        projectResponseDTO.setMessage(message);
        projectResponseDTO.setPayload(payloadDTO);
        return projectResponseDTO;
    }

    public Project findProject(int id) throws Exception {
        Project projectFound = projectRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Record not found"));
        if (!projectFound.getIsActive())
            throw new InactiveDataException("Project is already deleted.");
        return projectFound;
    }
}
