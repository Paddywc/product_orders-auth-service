package product.orders.authservice.domain.exception;

/**
 * Exception thrown when trying to register a user with an email address that already exists in the database.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("A user with this email already exists.");
    }
}