package com.brightroute.brightroute.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class AccessCodeUsedException extends RuntimeException {
    public AccessCodeUsedException(String message) {
        super(message);
    }
}