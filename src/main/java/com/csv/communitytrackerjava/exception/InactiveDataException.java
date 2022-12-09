package com.csv.communitytrackerjava.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InactiveDataException extends Exception {
    public InactiveDataException(String message) {
        super(message);
    }
}
