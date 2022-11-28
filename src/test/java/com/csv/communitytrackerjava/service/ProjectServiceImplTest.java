package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.ProjectAddDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import com.csv.communitytrackerjava.mapper.ProjectMapperImpl;
import com.csv.communitytrackerjava.model.Project;
import com.csv.communitytrackerjava.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {
    Project save;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    ProjectMapperImpl projectMapper;
    @Spy
    ModelMapper modelMapper;
    @InjectMocks
    ProjectServiceImpl projectServiceImpl;

    @BeforeEach
    void setup() {
        save = new Project(1, "Description desc", "Code123", true);

    }

    @Test
    public void saveProject() throws ProjectCodeExistException {
        //Arrange
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        when(projectRepository.save(any(Project.class))).thenReturn(save);
        when(projectMapper.validationToModel(any(ProjectAddDTO.class))).thenReturn(save);
        ProjectAddDTO projectAddDTO1 = modelMapper.map(save, ProjectAddDTO.class);

        //Act
        projectMapper.validationToModel(projectAddDTO1);
        ProjectResponseDTO expectedProject = projectServiceImpl.saveProject(projectAddDTO1);
        projectResponseDTO.setMessage("Successfully add project.");
        //Assert
        assertEquals(expectedProject.getMessage(),projectResponseDTO.getMessage());
    }
    @Test
    public void saveProjectWhenDuplicateData() throws ProjectCodeExistException {
        //Arrange
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        when(projectRepository.save(any(Project.class))).thenReturn(save);
        when(projectMapper.validationToModel(any(ProjectAddDTO.class))).thenReturn(save);

        ProjectAddDTO projectAddDTO1 = modelMapper.map(save, ProjectAddDTO.class);

        //Act
        projectMapper.validationToModel(projectAddDTO1);
        ProjectResponseDTO expectedProject = projectServiceImpl.saveProject(projectAddDTO1);
        ProjectResponseDTO expectedProject1 = projectServiceImpl.saveProject(projectAddDTO1);
        projectResponseDTO.setMessage("Successfully add project.");
        //Assert
        assertThrows(ProjectCodeExistException.class, () -> projectServiceImpl.saveProject(projectAddDTO1));
    }

}