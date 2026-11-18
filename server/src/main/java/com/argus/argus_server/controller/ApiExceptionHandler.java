package com.argus.argus_server.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

/** Turns bad input into a clear 400 for every endpoint, instead of a 500 error. */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> unreadableBody(HttpMessageNotReadableException e) {
        return error("The request body is not valid JSON, or a field has the wrong type");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> badUrlValue(MethodArgumentTypeMismatchException e) {
        return error("Invalid value in the URL for " + e.getName());
    }

    // the database rejected it: a value too long, a missing required value, or an ID that doesn't exist
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> rejectedByDatabase(DataIntegrityViolationException e) {
        return error("A value is too long, missing, or refers to something that doesn't exist");
    }

    // thrown by the models' own checks, e.g. VictimCase.updateStatus("banana")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> invalidValue(IllegalArgumentException e) {
        return error(e.getMessage());
    }

    private static ResponseEntity<Map<String, String>> error(String message) {
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}
