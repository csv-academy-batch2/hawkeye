package com.csv.communitytrackerjava.controller;

import com.csv.communitytrackerjava.dto.ProjectDTO;
import com.csv.communitytrackerjava.dto.ProjectPayloadDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.service.ExceptionService;
import com.csv.communitytrackerjava.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ProjectControllerGetAllProjectTest {
    ProjectDTO jojoProj, baquiProj, lyoydProj;


    List<ProjectDTO> projectList;


    Pageable pageable;

    Page<ProjectDTO> pageProjectList;


    @MockBean
    private ProjectService projectService;

    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        jojoProj = new ProjectDTO(1, "projectDesc1", "projectCode1");
        baquiProj = new ProjectDTO(2, "projectDesc2", "projectCode2");
        lyoydProj = new ProjectDTO(3, "projectDesc3", "projectCode3");


        projectList = List.of(jojoProj, baquiProj, lyoydProj);


        pageable = PageRequest.of(0, 3);


        pageProjectList = new PageImpl<>(projectList, pageable, projectList.size());

    }

    @Test
    void findAllProjectTest() throws Exception {
        //Arrange


        when(projectService.findAllProject(Mockito.any())).thenReturn(pageProjectList);

        //Act
        mockMvc.perform(get("/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", CoreMatchers.is(List.of(jojoProj, baquiProj, lyoydProj).size())))
                .andExpect(jsonPath("$.content[0].projectId", CoreMatchers.is(jojoProj.getProjectId())))
                .andExpect(jsonPath("$.content[1].projectId", CoreMatchers.is(baquiProj.getProjectId())))
                .andExpect(jsonPath("$.content[2].projectId", CoreMatchers.is(lyoydProj.getProjectId())))
                .andExpect(jsonPath("$.pageable.pageNumber", CoreMatchers.is(0)));
    }

    @Test
    void findProjectByProjectIdTest() throws Exception {
        //Arrange

        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        ProjectPayloadDTO projectPayloadDTO = new ProjectPayloadDTO();
        projectResponseDTO.setErrors(null);
        projectResponseDTO.setMessage("Successfully fetch");
        projectPayloadDTO.setAdditionalProperty("projects", jojoProj);
        projectResponseDTO.setPayload(projectPayloadDTO);

        when(projectService.findProjectById(anyInt()))
                .thenReturn(projectResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jojoProj)))
                .andExpect(jsonPath("$.payload.projects.projectId", CoreMatchers.is(jojoProj.getProjectId())))
                .andExpect(jsonPath("$.message", CoreMatchers.is(projectResponseDTO.getMessage())))
                .andExpect(status().isOk());
    }
}

