package edu.ncsu.csc326.wolfcafe.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception for WolfCafe API calls.
 */
@Getter
@AllArgsConstructor
public class WolfCafeAPIException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    /**
     * Response status associated with the exception
     */
    private final HttpStatus  status;
    /**
     * Error message associated with the exception
     */
    private final String      message;
}
