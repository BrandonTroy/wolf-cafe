package edu.ncsu.csc326.wolfcafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when a resource is not found.
 */
@ResponseStatus ( value = HttpStatus.NOT_FOUND )
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Exception to throw if an expected entry is not found in a database
     *
     * @param message
     *            error message associated with the exception
     */
    public ResourceNotFoundException ( final String message ) {
        super( message );
    }
}
