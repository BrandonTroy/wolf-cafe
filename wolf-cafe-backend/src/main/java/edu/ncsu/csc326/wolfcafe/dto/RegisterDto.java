package edu.ncsu.csc326.wolfcafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Information needed to register a new customer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    
    /** The name of the customer/guest being registered in the system */
    private String name;
    
    /** The username of the customer/guest being registered in the system */
    private String username;
    
    /** The email of the customer/guest being registered in the system */
    private String email;
    
    /** The password of the customer/guest being registered in the system */
    private String password;
}
