/**
 *
 */
package edu.ncsu.csc326.wolfcafe.mapper;

import edu.ncsu.csc326.wolfcafe.entity.Role;

/**
 * Maps Roles from the Role enum to their string representation
 */
public class RoleMapper {
    /**
     * Maps a Role to a String
     *
     * @param role
     *            Role to map
     * @return String representing the Role
     */
    public static String toString ( final Role role ) {
        switch ( role ) {
            case ADMIN:
                return "ROLE_ADMIN";
            case MANAGER:
                return "ROLE_MANAGER";
            case BARISTA:
                return "ROLE_BARISTA";
            case CUSTOMER:
                return "ROLE_CUSTOMER";
            default:
                return "ROLE_GUEST";
        }
    }
}
