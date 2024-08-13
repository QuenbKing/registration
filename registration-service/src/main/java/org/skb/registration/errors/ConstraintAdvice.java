package org.skb.registration.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ConstraintAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getBindingResult().getFieldErrors()
                        .stream()
                        .map(ex -> "ValidationError: " + ex.getDefaultMessage())
                        .collect(Collectors.joining("\n"))));
    }

    @ExceptionHandler({RegistrationException.class})
    public ResponseEntity<ErrorResponse> handleRegistration(RegistrationException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.CONFLICT.toString(), exception.getMessage()));
    }
}
