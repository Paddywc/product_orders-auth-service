package product.orders.authservice.domain.security;


/**
 * Interface for password matching functionality.
 */
public interface PasswordMatcher {
    boolean matches(String rawPassword, String passwordHash);
}
