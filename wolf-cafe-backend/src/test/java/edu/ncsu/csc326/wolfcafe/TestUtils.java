package edu.ncsu.csc326.wolfcafe;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.Gson;

import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;

/**
 * Class for handy utils shared across all of the API tests
 *
 * @author Kai Presler-Marshall and Yavin Hamm
 */
@SpringBootTest
public class TestUtils {

    /**
     * Object that handles converting to Json
     */
    private static Gson    gson = new Gson();

    /**
     * Connection to the UserRepository to delete the admin
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Uses Google's GSON parser to serialize a Java object to JSON. Useful for
     * creating JSON representations of our objects when calling API methods.
     *
     * @param obj
     *            to serialize to JSON
     * @return JSON string associated with object
     */
    public static String asJsonString ( final Object obj ) {
        return gson.toJson( obj );
    }

    /**
     * This test is here to ensure the admin user is deleted before the system
     * starts for the login test so that we get coverage on the creation of the
     * admin user.
     */
    @Test
    public void deleteAdmin () {
        Optional<User> admin = userRepository.findByUsername( "admin" );
        if ( admin.isPresent() ) {
            userRepository.delete( admin.get() );
        }
        admin = userRepository.findByUsername( "admin" );
        assertTrue( admin.isEmpty() );
    }

}
