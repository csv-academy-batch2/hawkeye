package com.csv.communitytrackerjava.controller;


import com.csv.communitytrackerjava.dto.ProjectDTO;
import com.csv.communitytrackerjava.dto.ProjectPayloadDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.dto.ProjectUpdateDTO;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import com.csv.communitytrackerjava.exception.RecordNotFoundException;
import com.csv.communitytrackerjava.service.ExceptionService;
import com.csv.communitytrackerjava.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest

public class ProjectControllerPatchTest {
    ProjectDTO jojoProj;

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
        jojoProj = new ProjectDTO(1, "projectDesc", "ProjectCode");


    }

    @Test
    void updateProjectSuccess() throws Exception {
        //Arrange

        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        ProjectPayloadDTO projectPayloadDTO = new ProjectPayloadDTO();
        projectResponseDTO.setErrors(null);
        projectResponseDTO.setMessage("Successfully update project.");
        projectPayloadDTO.setAdditionalProperty("projects", jojoProj);
        projectResponseDTO.setPayload(projectPayloadDTO);


        when(projectService.updateProject(any(ProjectUpdateDTO.class), anyInt()))
                .thenReturn(projectResponseDTO);


        //ACT
        mockMvc.perform(patch("/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jojoProj)))
                .andExpect(jsonPath("$.payload.projects.projectDesc", CoreMatchers.is(jojoProj.getProjectDesc())))
                .andExpect(jsonPath("$.payload.projects.projectCode", CoreMatchers.is(jojoProj.getProjectCode())))
                .andExpect(jsonPath("$.message", CoreMatchers.is(projectResponseDTO.getMessage())))
                .andExpect(status().isAccepted());


    }

    @Test
    void updateProjectDuplicate() throws Exception {
        //Arrange


        when(projectService.updateProject(any(ProjectUpdateDTO.class), anyInt()))
                .thenThrow(new ProjectCodeExistException("Project code already exist."));


        //ACT
        mockMvc.perform(patch("/projects/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jojoProj)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProjectCodeExistException))
                .andExpect(result -> assertEquals("Project code already exist.", Objects
                        .requireNonNull(result.getResolvedException()).getMessage()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProjectRecordNotFound() throws Exception {


        when(projectService.updateProject(any(ProjectUpdateDTO.class), anyInt()))
                .thenThrow(new RecordNotFoundException("Record not found."));

        //ACT
        mockMvc.perform(patch("/projects/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jojoProj)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("Record not found.", Objects
                        .requireNonNull(result.getResolvedException()).getMessage()))
                .andExpect(status().isBadRequest());

    }


}


