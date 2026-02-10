package product.orders.authservice.domain.security;


public interface PasswordMatcher {
    boolean matches(String rawPassword, String passwordHash);
}
