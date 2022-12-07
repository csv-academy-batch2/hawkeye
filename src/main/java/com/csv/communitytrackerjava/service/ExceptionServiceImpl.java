package com.csv.communitytrackerjava.service;

import com.csv.communitytrackerjava.dto.ApiErrorDTO;
import com.csv.communitytrackerjava.dto.ProjectPayloadDTO;
import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ExceptionServiceImpl implements ExceptionService {

    @Override
    public ProjectResponseDTO formatBadRequest(Exception exception) {

        // Init
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        ProjectPayloadDTO projectPayloadDTO = new ProjectPayloadDTO();
        ApiErrorDTO apiError = new ApiErrorDTO(BAD_REQUEST);

        // Setters
        apiError.setType(exception.getClass().getSimpleName());
        projectResponseDTO.setMessage(exception.getMessage());
        projectPayloadDTO.setAdditionalProperty("projects", Collections.emptyList());

        // Return
        return toProjectResponseDTO(apiError, exception.getMessage(), projectPayloadDTO);
    }

    @Override
    public ProjectResponseDTO formatBadRequest(Exception exception, String message) {

        // Init
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        ProjectPayloadDTO projectPayloadDTO = new ProjectPayloadDTO();
        ApiErrorDTO apiError = new ApiErrorDTO(BAD_REQUEST);

        // Setters
        apiError.setType(exception.getClass().getSimpleName());
        projectResponseDTO.setMessage(message);
        projectPayloadDTO.setAdditionalProperty("projects", Collections.emptyList());

        // Return
        return toProjectResponseDTO(apiError, message, projectPayloadDTO);
    }

    public ProjectResponseDTO toProjectResponseDTO(ApiErrorDTO apiError, String message, ProjectPayloadDTO projectPayloadDTO) {

        // Settings response
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        projectResponseDTO.setErrors(List.of(apiError));
        projectResponseDTO.setMessage(message);
        projectResponseDTO.setPayload(projectPayloadDTO);
        return projectResponseDTO;
    }

}
