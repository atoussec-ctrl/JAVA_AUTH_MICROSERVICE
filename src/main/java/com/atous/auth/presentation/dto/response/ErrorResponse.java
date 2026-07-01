package com.atous.auth.presentation.dto.response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(int status, String error, String message, String path, Instant timestamp, List<FieldErrorResponse> fieldErrors) {
    public static ErrorResponse of(int status, String error, String message, String path){return new ErrorResponse(status,error,message,path,Instant.now(),List.of());}
    public static ErrorResponse withFieldErrors(int status, String error, String message, String path, List<FieldErrorResponse> fieldErrors){return new ErrorResponse(status,error,message,path,Instant.now(),fieldErrors);}
}
