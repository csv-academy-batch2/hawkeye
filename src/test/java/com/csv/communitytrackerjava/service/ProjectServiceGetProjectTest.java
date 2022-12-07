package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.ProjectDTO;
import com.csv.communitytrackerjava.dto.ProjectPayloadDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceGetProjectTest {
    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService = new ProjectServiceImpl();

    @Spy
    ProjectMapper projectMapper = new ProjectMapperImpl();

    @Spy
    ModelMapper modelMapper;

    Project sample1 = new Project(1, "This is active", "Code1", true);
    ProjectDTO sample1DTO = projectMapper.toDTO(sample1);
    Project sample2 = new Project(2, "This is active", "Code2", true);
    Project sample3 = new Project(3, "This is not active", "Code3", false);

    List<Project> samples = List.of(sample1, sample2, sample3);
    Pageable pageable = PageRequest.of(1, 2);

    @Test
    public void testFindAllActiveProject() {
        Mockito.when(projectRepository.findByIsActiveTrue(any()))
                .thenReturn(samples);

        Page<ProjectDTO> result = projectService.findAllProject(pageable);

        Mockito.verify(projectRepository).findByIsActiveTrue(Mockito.any());
        assertEquals(2, result.getPageable().getPageSize());
        assertEquals(1, result.getPageable().getPageNumber());
    }

    @SneakyThrows
    @Test
    public void testFindProjectById() {
        Mockito.when(projectRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(sample1));

        ProjectResponseDTO result = projectService.findProjectById(1);

        ProjectResponseDTO expected = new ProjectResponseDTO();
        ProjectPayloadDTO samplePayload = new ProjectPayloadDTO();
        samplePayload.setAdditionalProperty("projects", sample1DTO);
        expected.setMessage("Successfully fetch");
        expected.setPayload(samplePayload);

        assertEquals(expected, result);
    }

    @SneakyThrows
    @Test
    public void testFindProjectByIdRecordNotFound() {
        Throwable throwable = catchThrowable(() -> projectService.findProjectById(anyInt()));
        assertThat(throwable).isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("Record not found");
    }


}