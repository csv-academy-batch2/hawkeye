package com.csv.communitytrackerjava.controller;

import com.csv.communitytrackerjava.dto.ProjectPayloadDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.exception.InactiveDataException;
import com.csv.communitytrackerjava.exception.RecordNotFoundException;
import com.csv.communitytrackerjava.model.Project;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ProjectControllerDeleteTest {
    Project delete, save;
    @MockBean
    ProjectService projectService;
    @MockBean
    ExceptionService exceptionService;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        save = Project.builder()
                .projectId(1)
                .projectDesc("Description desc")
                .projectCode("Code123")
                .isActive(true)
                .build();

        delete = Project.builder()
                .projectId(1)
                .projectDesc("Description desc")
                .projectCode("Code123")
                .isActive(false)
                .build();


    }

    @Test
    public void delete() throws Exception {
        //Arrange
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        ProjectPayloadDTO projectPayloadDTO = new ProjectPayloadDTO();
        save.setIsActive(false);
        projectResponseDTO.setErrors(null);
        projectResponseDTO.setMessage("Successfully delete project.");
        projectPayloadDTO.setAdditionalProperty("projects", save);
        projectResponseDTO.setPayload(projectPayloadDTO);
        when(projectService.deleteProject(anyInt()))
                .thenReturn(projectResponseDTO);


        //Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(save)))
                .andExpect(jsonPath("$.payload.projects.isActive", CoreMatchers.is(save.getIsActive())))
                .andExpect(jsonPath("$.message", CoreMatchers.is(projectResponseDTO.getMessage())))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteRecordNotFound() throws Exception {
        //Arrange
        when(projectService.deleteProject(anyInt()))
                .thenThrow(new RecordNotFoundException("Project to delete is not found."));
        //Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(save)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteProjectThatWasAlreadyDeleted() throws Exception {
        //Arrange
        when(projectService.deleteProject(anyInt()))
                .thenThrow(new InactiveDataException("Project is already delete."));
        //Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(delete)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InactiveDataException))
                .andExpect(status().isBadRequest());
    }
}