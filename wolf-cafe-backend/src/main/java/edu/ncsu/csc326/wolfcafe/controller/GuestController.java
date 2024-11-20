import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.ncsu.csc326.wolfcafe.entity.Guest;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.repository.GuestRepository;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.GuestUserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private GuestUserService guestUserService;  // Service for guest user logic

    @Autowired
    private UserRepository userRepository;  // User repository to manage user entities

    @Autowired
    private OrderRepository orderRepository;  // Order repository for order management

	private GuestRepository guestRepository;

    // 1. Endpoint for "Continue as Guest"
    @PostMapping("/login")
    public ResponseEntity<String> continueAsGuest(HttpSession session) {
        // 1. Create a new guest user
        String guestUsername = guestUserService.createGuestUser();

        // 2. Optionally, save the guest user in the session (or a token if you're using JWT)
        Guest guestUser = guestRepository.findByUsername(guestUsername);
        session.setAttribute("guestUser", guestUser);  // Store guest user in session

        // 3. Return a response with the guest username or a success message
        return ResponseEntity.ok("Guest logged in as: " + guestUsername);
    }

    // 2. Endpoint for guest to place an order
    @PostMapping("/order")
    public ResponseEntity<String> placeOrder(@RequestParam String orderDetails, HttpSession session) {
        // Retrieve the guest user from session
        User guestUser = (User) session.getAttribute("guestUser");

        if (guestUser == null) {
            return ResponseEntity.status(400).body("Guest user not logged in.");
        }

        // Create a new order for the guest user
        guestUserService.createOrderForGuest(guestUser, orderDetails);

        // Return success message
        return ResponseEntity.ok("Order placed successfully for guest user: " + guestUser.getUsername());
    }

    // 3. Endpoint for guest to view their order history
    @GetMapping("/order-history")
    public ResponseEntity<List<Order>> getOrderHistory(HttpSession session) {
        // Retrieve the guest user from session
        User guestUser = (User) session.getAttribute("guestUser");

        if (guestUser == null) {
            return ResponseEntity.status(400).body(null);  // No guest user logged in
        }

        // Retrieve the order history for the guest user
        List<Order> orders = guestUserService.getOrderHistoryForGuest(guestUser);

        // Return the order history
        return ResponseEntity.ok(orders);
    }
}
