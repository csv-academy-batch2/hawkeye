package com.csv.communitytrackerjava.mapper;

import com.csv.communitytrackerjava.dto.*;
import com.csv.communitytrackerjava.model.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toDTO(Project model);
    Project validationToModel(ProjectAddDTO projectAddDTO);
    ProjectGetPeopleDTO toGetPeopleDTO(Project getPeopleDTO);
}