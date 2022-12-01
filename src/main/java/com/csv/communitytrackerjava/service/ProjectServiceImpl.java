package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.ProjectAddDTO;
import com.csv.communitytrackerjava.dto.ProjectPayloadDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.dto.ProjectUpdateDTO;
import com.csv.communitytrackerjava.exception.InactiveDataException;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import com.csv.communitytrackerjava.exception.RecordNotFoundException;
import com.csv.communitytrackerjava.mapper.ProjectMapper;
import com.csv.communitytrackerjava.model.Project;
import com.csv.communitytrackerjava.repository.ProjectRepository;
import org.apache.commons.text.CaseUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public ProjectResponseDTO updateProject(ProjectUpdateDTO projectUpdateDTO, int id) throws Exception {
        ProjectPayloadDTO payloadDTO = new ProjectPayloadDTO();

        Project projectFound = projectRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Record not found."));
        Optional<Project> projectCodeCheck = projectRepository.findByProjectCode(projectUpdateDTO.getProjectCode());
        String newDesc = projectUpdateDTO.getProjectDesc().isBlank() ? projectFound.getProjectDesc() : CaseUtils.toCamelCase(projectUpdateDTO.getProjectDesc(), true, ' ');
        projectUpdateDTO.setProjectDesc(newDesc);

        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true);
        checkIfItsInactive(projectFound);
        if (projectCodeCheck.isPresent() && projectCodeCheck.get().getIsActive()) {
            throw new ProjectCodeExistException("Project code already exist.");
        }

        projectFound.setProjectCode(projectUpdateDTO.getProjectCode());
        modelMapper.map(projectUpdateDTO, projectFound);
        payloadDTO.setAdditionalProperty("projects", projectMapper.toDTO(projectRepository.save(projectFound)));
        return toProjectResponseDTO("Successfully update project.", payloadDTO);
    }

    @Override
    public ProjectResponseDTO findAllProject() {
        ProjectPayloadDTO payloadDTO = new ProjectPayloadDTO();
        payloadDTO.setAdditionalProperty("projects", projectMapper.toListDTO(projectRepository.findAll()));
        return toProjectResponseDTO("Successfully fetch all projects.", payloadDTO);
    }

    @Override
    public ProjectResponseDTO deleteProject(int id) throws Exception {
        ProjectPayloadDTO payloadDTO = new ProjectPayloadDTO();
        Project projectFound = projectRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Project to delete is not found."));
        checkIfItsInactive(projectFound);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        projectFound.setIsActive(false);
        payloadDTO.setAdditionalProperty("projects", projectMapper.toDTO(projectRepository.save(projectFound)));
        return toProjectResponseDTO("Successfully delete project.", payloadDTO);
    }

    public ProjectResponseDTO toProjectResponseDTO(String message, ProjectPayloadDTO payloadDTO) {
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        projectResponseDTO.setMessage(message);
        projectResponseDTO.setPayload(payloadDTO);
        return projectResponseDTO;
    }
    public void checkIfItsInactive(Project project) throws Exception {
        if (!project.getIsActive()) {
            throw new InactiveDataException("Project is already deleted.");
        }
    }
}
