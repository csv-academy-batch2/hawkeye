package com.csv.communitytrackerjava.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.annotation.Generated;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonPropertyOrder({
        "errors",
        "message",
        "payload"
})
public class ProjectResponseDTO {

    @JsonProperty("errors")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> errors = Collections.emptyList();
    @JsonProperty("message")
    private String message;
    @JsonProperty("payload")
    private ProjectPayloadDTO payload;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}