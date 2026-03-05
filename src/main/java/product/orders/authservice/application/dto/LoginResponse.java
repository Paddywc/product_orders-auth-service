package product.orders.authservice.application.dto;

/**
 * Response from the auth controller after a successful login
 * @param token JWT token for the authenticated user
 */
public record LoginResponse(String token) {
}

