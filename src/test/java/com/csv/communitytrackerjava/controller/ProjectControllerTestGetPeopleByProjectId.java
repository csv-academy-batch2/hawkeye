package com.csv.communitytrackerjava.controller;

import com.csv.communitytrackerjava.dto.ProjectGetPeopleDTO;
import com.csv.communitytrackerjava.exception.RecordNotFoundException;
import com.csv.communitytrackerjava.model.People;
import com.csv.communitytrackerjava.service.ExceptionService;
import com.csv.communitytrackerjava.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ProjectControllerTestGetPeopleByProjectId {
    @MockBean
    private ProjectService projectService;

    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    People peopleOne;
    ProjectGetPeopleDTO projectGetPeopleDTO;
    Pageable pageable;
    Pageable pageableTwo;
    Pageable pageableThree;
    List<ProjectGetPeopleDTO> projectList;
    List<ProjectGetPeopleDTO> projectListTwo;
    Page<ProjectGetPeopleDTO> pageProjectList;
    
    Page<ProjectGetPeopleDTO> pageProjectListTwo;
    Page<ProjectGetPeopleDTO> pageProjectListThree;
    
    People peopleTwo;
    ProjectGetPeopleDTO projectGetPeopleDTOTwo;

    ProjectGetPeopleDTO projectGetPeopleDTOThree;

    @BeforeEach
    void setup() {
        peopleOne = new People(1, 1, 1, "John Raven", "Tamang", "Glomar", "Job Level Desc", "CSV", "TestProjectDesc1", true);
        peopleTwo = new People(2, 2, 1, "John Raven", "Tamang", "Glomar", "Job Level Desc", "CSV", "TestProjectDesc1", true);
        projectGetPeopleDTO = new ProjectGetPeopleDTO(1, "TestProjectDesc1", "TPC1", true, List.of(peopleOne));
        projectGetPeopleDTOTwo = new ProjectGetPeopleDTO(2, "TestProjectDesc2", "TPC2", true, List.of(peopleTwo));
        projectGetPeopleDTOThree = new ProjectGetPeopleDTO(3, "TestProjectDesc3", "TPC3", false, List.of(peopleTwo));
        pageable = PageRequest.of(0, 1);
        pageableTwo = PageRequest.of(1, 1);
        pageableThree = PageRequest.of(0, 20);
        projectList = List.of(projectGetPeopleDTO, projectGetPeopleDTOTwo);
        projectListTwo = List.of(projectGetPeopleDTO, projectGetPeopleDTOTwo, projectGetPeopleDTOThree);
        pageProjectList = new PageImpl<>(projectList, pageable, projectList.size());
        pageProjectListTwo = new PageImpl<>(projectList, pageableTwo, projectList.size());
        pageProjectListThree = new PageImpl<>(projectListTwo, pageableThree, projectListTwo.size());
    }


    @Test
    @DisplayName("Find people by project id with pagination test")
    void findPeopleByProjectId() throws Exception {
        Mockito.when(projectService.findPeopleByProjectId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(pageProjectList);

        mockMvc.perform(get("/projects/people?projectId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size", CoreMatchers.is(pageProjectList.getSize())))
                .andExpect(jsonPath("$.content[0].projectId", CoreMatchers.is(pageProjectList.getContent().get(0).getProjectId())))
                .andExpect(jsonPath("$.content[0].peoples[0].projectId", CoreMatchers.is(pageProjectList.getContent().get(0).getPeoples().get(0).getProjectId())))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Find people by project id page 2")
    void findPeopleByProjectIdPage2() throws Exception {
        Mockito.when(projectService.findPeopleByProjectId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(pageProjectListTwo);

        mockMvc.perform(get("/projects/people")
                        .param("projectId", "1,2")
                        .param("page", "1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageProjectListTwo)))
                .andExpect(jsonPath("$.size", CoreMatchers.is(1)))
                .andExpect(jsonPath("$.totalPages", CoreMatchers.is(2)))
                .andExpect(jsonPath("$.totalElements", CoreMatchers.is(2)))
                .andExpect(status().isAccepted());
    }
    
    @Test
    @DisplayName("Find people by project id with pagination test")
    void findPeopleByProjectIdRecordNotFound() throws Exception {

        Mockito.when(projectService.findPeopleByProjectId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new RecordNotFoundException("Project doesn't exist."));

        mockMvc.perform(get("/projects/people?projectId=100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("Project doesn't exist.", result.getResolvedException().getMessage()))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Find people by project id with inactive projects")
    void findPeopleByProjectIdWithInactive() throws Exception {
        Mockito.when(projectService.findPeopleByProjectId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(pageProjectListThree);

        mockMvc.perform(get("/projects/people")
                        .param("projectId", "1,2,3")
                        .param("includeAll", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageProjectListThree)))
                .andExpect(jsonPath("$.numberOfElements", CoreMatchers.is(3)))
                .andExpect(status().isAccepted());
    }


}