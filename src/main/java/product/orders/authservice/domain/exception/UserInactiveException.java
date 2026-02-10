package product.orders.authservice.domain.exception;

import java.util.UUID;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException(UUID userId) {
        super("User is inactive: " + userId);
    }
}

