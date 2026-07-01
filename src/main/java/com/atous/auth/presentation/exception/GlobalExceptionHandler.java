package com.atous.auth.presentation.exception;

import com.atous.auth.domain.exception.*;
import com.atous.auth.presentation.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidCredentialsException.class) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse invalid(InvalidCredentialsException e,HttpServletRequest r){return ErrorResponse.of(401,"Unauthorized","Invalid credentials",r.getRequestURI());}
    @ExceptionHandler({TokenExpiredException.class, TokenRevokedException.class}) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse token(DomainException e,HttpServletRequest r){return ErrorResponse.of(401,"Unauthorized",e.getMessage(),r.getRequestURI());}
    @ExceptionHandler(UserAlreadyExistsException.class) @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse exists(UserAlreadyExistsException e,HttpServletRequest r){return ErrorResponse.of(409,"Conflict",e.getMessage(),r.getRequestURI());}
    @ExceptionHandler(UserNotFoundException.class) @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(UserNotFoundException e,HttpServletRequest r){return ErrorResponse.of(404,"Not Found",e.getMessage(),r.getRequestURI());}
    @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validation(MethodArgumentNotValidException e,HttpServletRequest r){List<FieldErrorResponse> fs=e.getBindingResult().getFieldErrors().stream().map(x->new FieldErrorResponse(x.getField(),x.getDefaultMessage())).toList(); return ErrorResponse.withFieldErrors(400,"Bad Request","Validation failed",r.getRequestURI(),fs);}
    @ExceptionHandler(DomainException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse domain(DomainException e,HttpServletRequest r){return ErrorResponse.of(400,"Bad Request",e.getMessage(),r.getRequestURI());}
}
