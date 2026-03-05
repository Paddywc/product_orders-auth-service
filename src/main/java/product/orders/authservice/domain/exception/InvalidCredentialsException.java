package product.orders.authservice.domain.exception;

/**
 * Exception thrown when provided email or password that does not match any user in the database.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}