package com.csv.communitytrackerjava.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiErrorDTO {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String type;

    public ApiErrorDTO() {
        timestamp = LocalDateTime.now();
    }

    public ApiErrorDTO(HttpStatus status) {
        this();
        this.status = status;
    }


    ApiErrorDTO(HttpStatus status, String message) {
        this();
        this.status = status;
        this.type = message;
    }


}