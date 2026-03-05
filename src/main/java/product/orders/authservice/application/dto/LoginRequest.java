package product.orders.authservice.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * Request to authenticate a user sent to the auth controller
 * @param email the email of the user
 * @param password raw password of the user
 */
public record LoginRequest(@Email String email, @NotEmpty String password) {
}
