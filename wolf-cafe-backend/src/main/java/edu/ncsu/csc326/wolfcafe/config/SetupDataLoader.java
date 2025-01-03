package edu.ncsu.csc326.wolfcafe.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;

/**
 * Sets up the database with roles and a default admin user. Based on code from
 * https://github.com/Baeldung/spring-security-registration/blob/master/src/main/java/com/baeldung/spring/SetupDataLoader.java
 */
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    /** True if already setup */
    private boolean         alreadySetup = false;

    /** Link to UserRepository */
    @Autowired
    private UserRepository  userRepository;

    /** Encodes passwords */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** Admin password in application.properties file */
    @Value ( "${app.admin-user-password}" )
    private String          adminUserPassword;

    /**
     * When the application loads and the context is refreshed this method will
     * run and create the admin user role and any other user roles defined in
     * the Roles.UserRoles enum.
     */
    @Override
    @Transactional
    public void onApplicationEvent ( final ContextRefreshedEvent event ) {
        if ( alreadySetup ) {
            return;
        }

        createUserIfNotFound( "Admin User", "admin", "admin@admin.edu", Role.ADMIN );

        alreadySetup = true;
    }

    /**
     * Creates a user with the given information
     *
     * @param name
     *            user's name
     * @param username
     *            user's username
     * @param email
     *            user's email
     * @param role
     *            user's role
     * @return created user
     */
    @Transactional
    public User createUserIfNotFound ( final String name, final String username, final String email, final Role role ) {
        final Optional<User> returnedUser = userRepository.findByUsernameOrEmail( username, email );

        if ( returnedUser.isEmpty() ) {
            final User user = new User();
            user.setName( name );
            user.setUsername( username );
            user.setEmail( email );
            user.setPassword( passwordEncoder.encode( adminUserPassword ) );
            user.setRole( role );
            userRepository.save( user );
            return user;
        }
        else {
            return returnedUser.get();
        }

    }

}
