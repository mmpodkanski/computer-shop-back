package io.github.mmpodkanski.computershop.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

class ApiException {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    ApiException(
            String message,
            HttpStatus httpStatus,
            ZonedDateTime timestamp
    ) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    String getMessage() {
        return message;
    }

    HttpStatus getHttpStatus() {
        return httpStatus;
    }

    ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
