package product.orders.authservice.domain.model;

import java.util.UUID;

public record UserId(UUID value) {
    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }
}