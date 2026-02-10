package product.orders.authservice.infrastructure.security;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import product.orders.authservice.domain.security.PasswordMatcher;

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