package edu.ncsu.csc326.wolfcafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response for authenticated and authorized user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {

    /** The unique access token generated during authorization */
    private String accessToken;
    
    /** The type of token generated during authorization */
    private String tokenType = "Bearer";
    
    /** The  Role of the user authorizing as a string */
    private String role;
}
