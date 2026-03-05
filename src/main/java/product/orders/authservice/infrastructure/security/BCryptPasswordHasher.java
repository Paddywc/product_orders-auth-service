package product.orders.authservice.infrastructure.security;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import product.orders.authservice.domain.security.PasswordHasher;

/**
 * Wrapper for Spring's BCryptPasswordEncoder to implement PasswordHasher interface. Assumes that the config maps
 * a password encoder named "bcrypt" as the default encoder.
 */
@Component
public class BCryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordHasher(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}