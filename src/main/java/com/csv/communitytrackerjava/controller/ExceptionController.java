package com.csv.communitytrackerjava.controller;

import com.csv.communitytrackerjava.dto.ProjectResponseDTO;
import com.csv.communitytrackerjava.exception.InactiveDataException;
import com.csv.communitytrackerjava.exception.InvalidInputException;
import com.csv.communitytrackerjava.exception.ProjectCodeExistException;
import com.csv.communitytrackerjava.exception.RecordNotFoundException;
import com.csv.communitytrackerjava.service.ExceptionService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@ControllerAdvice
public class ExceptionController {


    @Autowired
    ExceptionService exceptionService;


    @ExceptionHandler({RecordNotFoundException.class, ProjectCodeExistException.class, InactiveDataException.class, InvalidInputException.class})
    public ResponseEntity<ProjectResponseDTO> handleRecordNotFoundException(Exception e) {
           return new ResponseEntity<>(exceptionService.formatBadRequest(e), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ProjectResponseDTO> handleMethodArgumentTypeMismatchException(Exception e) {
        return new ResponseEntity<>(exceptionService.formatBadRequest(e, "Invalid input type."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String field = Objects.requireNonNull(e.getFieldError()).getField();
        String code = e.getFieldError().getCode();
        assert code != null;
        String message = switch (code) {
            case "Size" -> "Size of " + field + " must not be more than 100";
            case "NotNull", "NotEmpty", "NotBlank" -> field + " is required";
            default -> "MethodArgumentNotValidException caught";
        };
        return new ResponseEntity<>(exceptionService.formatBadRequest(e, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormat(InvalidFormatException e) {
        String field = e.getPath().get(0).toString();
        String message = "Invalid data format on " + field;
        return new ResponseEntity<>(exceptionService.formatBadRequest(e, message), HttpStatus.BAD_REQUEST);
    }

}
