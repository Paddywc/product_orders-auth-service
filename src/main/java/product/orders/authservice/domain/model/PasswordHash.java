package product.orders.authservice.domain.model;

/**
 * Password hash value object.
 */
public record PasswordHash(String value) {

    public PasswordHash {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be blank");
        }
    }
}