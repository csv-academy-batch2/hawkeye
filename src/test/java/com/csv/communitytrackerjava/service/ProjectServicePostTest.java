package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.ProjectAddDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import com.csv.communitytrackerjava.mapper.ProjectMapper;
import com.csv.communitytrackerjava.mapper.ProjectMapperImpl;
import com.csv.communitytrackerjava.model.Project;
import com.csv.communitytrackerjava.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectServicePostTest {
    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService = new ProjectServiceImpl();

    @Spy
    ProjectMapper projectMapper = new ProjectMapperImpl();

    ProjectAddDTO baqui = new ProjectAddDTO(1, "project desc", "project code", true);
    Project BaquiToModel = projectMapper.validationToModel(baqui);

    @Test
    public void testSaveProject() throws ProjectCodeExistException {
        Mockito.when(projectRepository.save(any(Project.class))).thenReturn(BaquiToModel);

        ProjectResponseDTO save = projectService.saveProject(baqui);

//        Project expected = new Project(1, "ProjectDesc", "project code", true);
        Project expected = Project.builder()
                .projectId(1)
                .projectDesc("ProjectDesc")
                .projectCode("project code")
                .isActive(true)
                .build();

        Mockito.verify(projectRepository).save(expected);
        assertThat(save.equals(expected));
    }

    @Test
    public void testSaveCodeExistValidation() {
        Mockito.when(projectRepository.save(any(Project.class))).thenReturn(BaquiToModel);

        Mockito.when(projectRepository.findByProjectCode(baqui.getProjectCode()))
                .thenReturn(Optional.ofNullable(BaquiToModel));

        Throwable throwable = catchThrowable(()->Optional.ofNullable(projectService.saveProject(baqui)));
        assertThat(throwable).isInstanceOf(ProjectCodeExistException.class)
                .hasMessageContaining("Project code already exist.");
    }

}


