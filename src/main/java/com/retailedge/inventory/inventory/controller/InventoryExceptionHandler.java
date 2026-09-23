package com.retailedge.inventory.inventory.controller;

import com.retailedge.inventory.inventory.service.InventoryAlreadyExistsException;
import com.retailedge.inventory.inventory.service.InventoryAuthorizationException;
import com.retailedge.inventory.inventory.service.InventoryConcurrencyException;
import com.retailedge.inventory.inventory.service.InventoryNotFoundException;
import com.retailedge.inventory.inventory.service.InventoryValidationException;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class InventoryExceptionHandler {

    @ExceptionHandler(InventoryNotFoundException.class)
    ProblemDetail handleNotFound() {
        return problem(HttpStatus.NOT_FOUND, "Inventory resource was not found");
    }

    @ExceptionHandler(InventoryAlreadyExistsException.class)
    ProblemDetail handleConflict() {
        return problem(HttpStatus.CONFLICT, "Inventory already exists");
    }

    @ExceptionHandler(InventoryConcurrencyException.class)
    ProblemDetail handleConcurrency() {
        return problem(HttpStatus.CONFLICT, "Inventory changed concurrently; retry the request");
    }

    @ExceptionHandler(InventoryAuthorizationException.class)
    ProblemDetail handleAuthorization() {
        return problem(HttpStatus.FORBIDDEN, "You are not authorized to access this inventory");
    }

    @ExceptionHandler({InventoryValidationException.class, ConstraintViolationException.class})
    ProblemDetail handleDomainValidation() {
        return problem(HttpStatus.BAD_REQUEST, "The inventory request is invalid");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleBodyValidation(MethodArgumentNotValidException exception) {
        ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "The inventory request is invalid");
        String fields = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField())
                .distinct()
                .sorted()
                .collect(Collectors.joining(","));
        problem.setProperty("fields", fields);
        return problem;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ProblemDetail handlePathValidation() {
        return problem(HttpStatus.BAD_REQUEST, "The inventory request is invalid");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail handleDataIntegrity() {
        return problem(HttpStatus.CONFLICT, "The inventory request conflicts with existing data");
    }

    private ProblemDetail problem(HttpStatus status, String detail) {
        return ProblemDetail.forStatusAndDetail(status, detail);
    }
}
