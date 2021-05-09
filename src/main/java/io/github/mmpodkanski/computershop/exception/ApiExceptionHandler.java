package io.github.mmpodkanski.computershop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiBadRequestException.class})
    ResponseEntity<Object> handleApiBadRequestException(ApiBadRequestException e) {
        var badRequest = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(exception(e, badRequest), badRequest);
    }

    @ExceptionHandler(value = {ApiNotFoundException.class})
    ResponseEntity<Object> handleApiNotFoundException(ApiNotFoundException e) {
        var notFound = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(exception(e, notFound), notFound);
    }

    private ApiException exception(RuntimeException e, HttpStatus status) {
        return new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }
}
