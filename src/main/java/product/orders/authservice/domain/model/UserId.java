package product.orders.authservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

/**
 * Value object representing a user ID.
 */
@Embeddable
public class UserId {

    @Column(name = "id", nullable = false, updatable = false)
    private UUID value;

    protected UserId() {
        // for JPA
    }

    private UserId(UUID value) {
        this.value = value;
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return value.equals(userId.value);
    }
}