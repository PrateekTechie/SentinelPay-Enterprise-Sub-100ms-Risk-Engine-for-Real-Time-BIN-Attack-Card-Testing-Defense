
package com.example.demo.dto;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        T data,
        Map<String, String> validationErrors) {
}