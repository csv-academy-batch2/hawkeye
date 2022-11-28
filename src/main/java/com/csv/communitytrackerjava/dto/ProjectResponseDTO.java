package com.csv.communitytrackerjava.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Generated;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({
        "errors",
        "message",
        "payload"
})
@Generated("jsonschema2pojo")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectResponseDTO {

    @JsonProperty("errors")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> errors = Collections.emptyList();
    @JsonProperty("message")
    private String message;
    @JsonProperty("payload")
    private ProjectPayloadDTO payload;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    @JsonProperty("errors")
    public List<Object> getErrors() {
        return errors;
    }

    @JsonProperty("errors")
    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("payload")
    public ProjectPayloadDTO getPayload() {
        return payload;
    }

    @JsonProperty("payload")
    public void setPayload(ProjectPayloadDTO payload) {
        this.payload = payload;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}