import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.dto.GuestDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Guest;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.mapper.UserMapper;
import edu.ncsu.csc326.wolfcafe.repository.GuestRepository;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.GuestUserService;

import java.util.List;

@Service
public class GuestUserServiceImpl implements GuestUserService {

    @Autowired
    private UserRepository userRepository;  // User repository to manage user entities

    @Autowired
    private OrderRepository orderRepository;  // Order repository for order management

	private GuestRepository guestRepository;

    @Override
    public String createGuestUser() {
        // Get the current user count from the repository
        long userCount = userRepository.count();  // Get the count of users

        // Generate a new guest username based on the count
        String guestUsername = "guest-" + (userCount + 1);  // Create a unique username

        // Create a new user entity for the guest
        User newUser = new User();
        newUser.setUsername(guestUsername);
        newUser.setRole(Role.GUEST); // Set the role as guest

        // Save the new guest user to the database
        userRepository.save(newUser);

        // Return the guest username
        return guestUsername;
    }

    @Override
    public void createOrderForGuest(User guestUser, String orderDetails) {
        // Create a new order object
        Order newOrder = new Order();
        newOrder.setGuestUser(guestUser);  // Link the order to the guest user
        newOrder.setOrderDetails(orderDetails);  // Set the order details

        // Save the new order to the database
        orderRepository.save(newOrder);
    }

    @Override
    public List<Order> getOrderHistoryForGuest(User guestUser) {
        // Retrieve the orders for the specific guest user
        return orderRepository.findByGuestUser(guestUser);
    }

    public GuestDto getUser ( final Long id ) {
        final Guest user = guestRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "User does not exist with id " + id ) );
        return GuestMapper.mapToGuestDto( user );
    }
    
	@Override
	public GuestDto getUserById(Long userId) {
		return getUser( userId ); // Reuse the existing method
	}
}
