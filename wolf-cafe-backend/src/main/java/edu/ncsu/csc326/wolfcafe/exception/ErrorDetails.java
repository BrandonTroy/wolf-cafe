package edu.ncsu.csc326.wolfcafe.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Provides details on errors.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {

    /**
     * Time the error occurred
     */
    private LocalDateTime timeStamp;
    /**
     * Summary or title of the error
     */
    private String        message;
    /**
     * More details about the error
     */
    private String        details;
}
