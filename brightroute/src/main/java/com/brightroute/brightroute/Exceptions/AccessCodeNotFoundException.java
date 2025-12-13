package com.brightroute.brightroute.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404
public class AccessCodeNotFoundException extends RuntimeException {
    public AccessCodeNotFoundException(String message) {
        super(message);
    }
}