package edu.ncsu.csc326.wolfcafe.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Handles global errors.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles errors on api calls from the frontend
     *
     * @param exception
     *            The error that happened during the api call
     * @param webRequest
     *            info about the request that led to the error
     * @return details about the error that occurred
     */
    @ExceptionHandler ( WolfCafeAPIException.class )
    public ResponseEntity<ErrorDetails> handleAPIException ( final WolfCafeAPIException exception,
            final WebRequest webRequest ) {
        final ErrorDetails errorDetails = new ErrorDetails( LocalDateTime.now(), exception.getMessage(),
                webRequest.getDescription( false ) );

        return new ResponseEntity<>( errorDetails, HttpStatus.BAD_REQUEST );
    }
}
