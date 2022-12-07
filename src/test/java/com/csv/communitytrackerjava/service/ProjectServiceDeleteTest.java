package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.exception.InactiveDataException;
import com.csv.communitytrackerjava.mapper.ProjectMapperImpl;
import com.csv.communitytrackerjava.model.Project;
import com.csv.communitytrackerjava.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceDeleteTest {
    Project save,deleted;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    ProjectMapperImpl projectMapper;
    @InjectMocks
    ProjectServiceImpl projectServiceImpl;
    @Spy
    ModelMapper modelMapper;

    @BeforeEach
    void setup() {
        
        save = Project.builder()
                .projectId(1)
                .projectDesc("Description desc")
                .projectCode("Code123")
                .isActive(true)
                .build();

        deleted = Project.builder()
                .projectId(1)
                .projectDesc("Description desc")
                .projectCode("Code123")
                .isActive(false)
                .build();

    }
    @Test
    public void deleteProject() throws Exception {
        //Arrange
        Mockito.when(projectRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(save));
        //Act
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        projectServiceImpl.deleteProject(1);
        //Assert
        Mockito.verify(projectRepository).save(save);
    }
    @Test
    public void deleteProjectByFindingId() throws Exception {
        //Arrange
        Mockito.when(projectRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(save));
        //Act
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        projectServiceImpl.deleteProject(1);
        //Assert
        Mockito.verify(projectRepository).findById(1);
    }
    @Test
    public void deleteProjectThatAlreadyDeleted() {
        //Arrange
        Mockito.when(projectRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(deleted));
        //Act

        //Assert
        assertThrows(InactiveDataException.class, () -> projectServiceImpl.deleteProject(1));

    }
}