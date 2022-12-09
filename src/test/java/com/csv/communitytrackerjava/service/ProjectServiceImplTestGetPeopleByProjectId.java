package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.ProjectGetPeopleDTO;
import com.csv.communitytrackerjava.exception.RecordNotFoundException;
import com.csv.communitytrackerjava.mapper.ProjectMapper;
import com.csv.communitytrackerjava.mapper.ProjectMapperImpl;
import com.csv.communitytrackerjava.model.People;
import com.csv.communitytrackerjava.model.Project;
import com.csv.communitytrackerjava.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTestGetPeopleByProjectId {
    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService = new ProjectServiceImpl();

    @Spy
    ProjectMapper projectMapper = new ProjectMapperImpl();

    People peopleOne;
    Project projectTestOne;
    Project projectTestTwo;
    Project projectTestThree;
    Pageable pageable;
    List<Project> projectList;
    List<Project> projectListTwo;
    Set<Integer> idList;

    Pageable pageableTwo;
    Pageable pageableThree;
    
    

    @BeforeEach
    void setup() {
        peopleOne = new People(1, 1, 1, "John Raven", "Tamang", "Glomar", "Job Level Desc", "CSV", "TestProjectDesc1", true);
        projectTestOne = new Project(1, "TestProjectDesc1", "TPC1", true, List.of(peopleOne));
        projectTestTwo = new Project(2, "TestProjectDesc2", "TPC2", true, Collections.emptyList());
        projectTestThree = new Project(3, "TestProjectDesc3", "TPC3", false, List.of(peopleOne));
        pageable = PageRequest.of(0, 1);
        pageableTwo = PageRequest.of(1, 1);
        pageableThree = PageRequest.of(0, 20);
        projectList = List.of(projectTestOne, projectTestTwo);
        projectListTwo = List.of(projectTestOne, projectTestTwo, projectTestThree);
        idList = new HashSet<>(10);
        
    }

    @Test
    @DisplayName("Find people by project id with pagination test")
    void findPeopleByProjectId() throws Exception {

        Mockito.when(projectRepository.findAllByProjectIdIn(Mockito.any(), Mockito.any())).thenReturn(projectList);

        Page<ProjectGetPeopleDTO> result = projectService.findPeopleByProjectId(pageable, idList, false);

        Mockito.verify(projectRepository).findAllByProjectIdIn(Mockito.any(), Mockito.any());
        assertEquals(2, result.getContent().size());
        assertEquals("TPC1", result.getContent().get(0).getProjectCode());
        assertEquals("Glomar", result.getContent().get(0).getPeoples().get(0).getLastName());
    }

    @Test
    @DisplayName("Find people by project id page 2")
    void findPeopleByProjectIdPage2() throws Exception {
        Mockito.when(projectRepository.findAllByProjectIdIn(Mockito.any(), Mockito.any())).thenReturn(projectList);

        Page<ProjectGetPeopleDTO> result = projectService.findPeopleByProjectId(pageableTwo, idList, false);

        Mockito.verify(projectRepository).findAllByProjectIdIn(Mockito.any(), Mockito.any());
        assertEquals(1, result.getPageable().getPageSize());
        assertEquals(1, result.getPageable().getPageNumber());
    }

    @Test
    @DisplayName("Find people by project id with inactive data")
    void findPeopleByProjectIdWithInactive() throws Exception {
        Mockito.when(projectRepository.findAllByProjectIdInAndIsActiveTrue(Mockito.any(), Mockito.any())).thenReturn(projectListTwo);

        Page<ProjectGetPeopleDTO> result = projectService.findPeopleByProjectId(pageableThree, idList, true);

        Mockito.verify(projectRepository).findAllByProjectIdInAndIsActiveTrue(Mockito.any(), Mockito.any());
        assertEquals(3, result.getContent().size());
    }
    
    @Test
    @DisplayName("Find people by project id with record not found test")
    void findPeoplByProjectId() throws Exception {

        Throwable exception = assertThrows(RecordNotFoundException.class, () -> projectService.findPeopleByProjectId(pageable, idList, false));

        Mockito.verify(projectRepository).findAllByProjectIdIn(Mockito.any(), Mockito.any());
        assertEquals("Project doesn't exist", exception.getMessage());
    }
}