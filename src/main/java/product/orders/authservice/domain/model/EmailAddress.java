package product.orders.authservice.domain.model;

import java.util.regex.Pattern;

/**
 * Email address value object.
 * @param value email address value
 */
public record EmailAddress(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public EmailAddress {
        if (value == null) {
            throw new IllegalArgumentException("Email address cannot be null");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email address format");
        }
    }
    @Override
    public String toString() {
        return value;
    }
}
