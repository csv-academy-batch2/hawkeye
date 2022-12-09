package com.csv.communitytrackerjava.controller;

import com.csv.communitytrackerjava.dto.ProjectAddDTO;
import com.csv.communitytrackerjava.dto.ProjectPayloadDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import com.csv.communitytrackerjava.service.ExceptionService;
import com.csv.communitytrackerjava.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class     ProjectControllerPostTest {

    @MockBean
    private ProjectService projectService;
    @MockBean
    private ExceptionService exceptionService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    ProjectAddDTO baqui = new ProjectAddDTO(1, "project desc", "project code", true);

    @SneakyThrows
    @Test
    public void testSaveController() throws ProjectCodeExistException {

        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        ProjectPayloadDTO projectPayloadDTO = new ProjectPayloadDTO();

        projectResponseDTO.setErrors(null);
        projectResponseDTO.setMessage("Successfully add project.");
        projectPayloadDTO.setAdditionalProperty("projects", baqui);
        projectResponseDTO.setPayload(projectPayloadDTO);

        when(projectService.saveProject(any(ProjectAddDTO.class))).thenReturn(projectResponseDTO);
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baqui)))
                .andExpect(jsonPath("$.payload.projects.projectId",
                        CoreMatchers.is(baqui.getProjectId())))
                .andExpect(jsonPath("$.payload.projects.projectDesc",
                        CoreMatchers.is(baqui.getProjectDesc())))
                .andExpect(jsonPath("$.payload.projects.projectCode",
                        CoreMatchers.is(baqui.getProjectCode())))
                .andExpect(jsonPath("$.payload.projects.isActive",
                        CoreMatchers.is(baqui.getIsActive())))
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(projectResponseDTO.getMessage())))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    public void testSaveControllerCodeExistValidation() {

        when(projectService.saveProject(any(ProjectAddDTO.class)))
                .thenThrow(new ProjectCodeExistException("Project code already exist."));

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baqui)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProjectCodeExistException))
                .andExpect(result -> assertEquals("Project code already exist.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andExpect(status().isBadRequest());
    }
}
