package product.orders.authservice.domain.model;

public record EmailAddress(String value) {

    public EmailAddress {
        if (value == null) {
            throw new IllegalArgumentException("Email address cannot be null");
        }
        // Verify that it contains an @ with characters before and after
        if (!value.matches("^.+@.+$")) {
            throw new IllegalArgumentException("Invalid email address format");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
