package product.orders.authservice.infrastructure.security;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import product.orders.authservice.domain.security.PasswordMatcher;

/**
 * Wrapper for Spring's BCryptPasswordEncoder to implement PasswordMatcher interface. Assumes that the config maps
 *  * a password encoder named "bcrypt" as the default encoder.
 */
@Component
public class BCryptPasswordMatcher implements PasswordMatcher {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordMatcher(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash) {
        return passwordEncoder.matches(rawPassword, passwordHash);
    }
}