package product.orders.authservice.application;

import product.orders.authservice.domain.exception.EmailAlreadyExistsException;
import product.orders.authservice.application.dto.LoginRequest;
import product.orders.authservice.application.dto.RegisterUserRequest;

import java.util.UUID;

public interface AuthApplicationService {
    /**
     * Authenticate user with given credentials.
     * @param command user email address and raw password
     * @return JWT token for the authenticated user
     */
    String authenticate(LoginRequest command);

    /**
     * Register new user and save it to the database.
     * @param command user email address and raw password
     * @return id of the registered user
     */
    UUID registerUser(RegisterUserRequest command) throws EmailAlreadyExistsException;
}
