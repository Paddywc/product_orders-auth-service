package product.orders.authservice.domain.model;

import java.util.Set;
import java.util.UUID;

/**
 * JWT subject information.
 */
public record JwtSubject(UUID userId,
                         String email,
                         Set<String> roles) {
}
