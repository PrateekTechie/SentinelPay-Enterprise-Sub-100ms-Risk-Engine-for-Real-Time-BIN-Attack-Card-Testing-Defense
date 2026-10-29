package com.example.demo.exception;

public class InvalidPatchRequestException extends RuntimeException {

    public InvalidPatchRequestException(String message) {
        super(message);
    }
}