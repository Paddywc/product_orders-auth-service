package product.orders.authservice.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * Request to register a new user sent to the auth controller
 * @param email the email of the user
 * @param rawPassword raw password of the user
 */
public record RegisterUserRequest(@Email String email, @NotEmpty String rawPassword) {
}
