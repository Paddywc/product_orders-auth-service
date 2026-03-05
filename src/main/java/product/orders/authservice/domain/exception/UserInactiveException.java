package product.orders.authservice.domain.exception;

import java.util.UUID;

/**
 * Exception thrown if a user is inactive.
 */
public class UserInactiveException extends RuntimeException {
    public UserInactiveException(UUID userId) {
        super("User is inactive: " + userId);
    }
}

