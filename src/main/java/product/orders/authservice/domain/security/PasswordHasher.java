package product.orders.authservice.domain.security;

/**
 * Interface for password hashing functionality.
 */
public interface PasswordHasher {
    String hash(String rawPassword);
}
