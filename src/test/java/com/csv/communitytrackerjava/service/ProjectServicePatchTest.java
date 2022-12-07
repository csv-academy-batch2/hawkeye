package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.ProjectDTO;
import com.csv.communitytrackerjava.dto.ProjectUpdateDTO;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import com.csv.communitytrackerjava.exception.RecordNotFoundException;
import com.csv.communitytrackerjava.mapper.ProjectMapper;
import com.csv.communitytrackerjava.mapper.ProjectMapperImpl;
import com.csv.communitytrackerjava.model.Project;
import com.csv.communitytrackerjava.repository.ProjectRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class ProjectServicePatchTest {
    @Mock
    ProjectRepository projectRepository;
    @InjectMocks
    ProjectService projectService = new ProjectServiceImpl();
    @Spy
    ModelMapper modelMapper;
    @Spy
    ProjectMapper projectMapper = new ProjectMapperImpl();
//    Project sample = new Project(1, "projectCode", "description", true);
    Project sample = Project.builder()
            .projectId(1)
            .projectDesc("description")
            .projectCode("projectCode")
            .isActive(true)
            .build();

    ProjectDTO sampleDTO = new ProjectDTO(1, "projectCode", "description");

    @SneakyThrows
    @Test
    public void testUpdateDesc() throws RecordNotFoundException {
        Mockito.when(projectRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(sample));

        Mockito.when(projectMapper.toDTO(projectRepository.save(any(Project.class))))
                .thenReturn(sampleDTO);

        ProjectUpdateDTO newSample = new ProjectUpdateDTO("new description", "newProjectCode");

        projectService.updateProject(newSample, 1);

//        Project expected = new Project(1, "NewDescription", "newProjectCode", true);
        Project expected = Project.builder()
                .projectId(1)
                .projectDesc("NewDescription")
                .projectCode("newProjectCode")
                .isActive(true)
                .build();

        Mockito.verify(projectRepository).save(expected);
    }

    @Test
    public void testUpdateExistingCodeValidation() throws RecordNotFoundException {
        Mockito.when(projectRepository.findByProjectCode("projectCode"))
                .thenReturn(Optional.ofNullable(sample));
        Mockito.when(projectRepository.findById(1))
                .thenReturn(Optional.ofNullable(sample));

        ProjectUpdateDTO newSample = new ProjectUpdateDTO("new description", "projectCode");

        Throwable throwable = catchThrowable(() -> Optional.ofNullable(projectService.updateProject(newSample, 1)));
        assertThat(throwable).isInstanceOf(ProjectCodeExistException.class)
                .hasMessageContaining("Project code already exist.");
    }
}
