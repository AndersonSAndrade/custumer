package com.adsdev.costumer.resources.exceptions;

import com.adsdev.costumer.services.exceptions.DatabaseException;
import com.adsdev.costumer.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StanderError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
        return getStanderErrorResponseEntity(request,"Recurso Não Encontrado",  e.getMessage(), e, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StanderError> database(DatabaseException e, HttpServletRequest request){
        return getStanderErrorResponseEntity(request, "Integridade com o banco, entidade possue dados relacionados!", e.getMessage(), e, HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<StanderError> getStanderErrorResponseEntity(HttpServletRequest request, String title, String message, Exception e, HttpStatus status) {
        StanderError error = new StanderError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError(title);
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}