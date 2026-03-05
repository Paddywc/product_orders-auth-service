package product.orders.authservice.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import product.orders.authservice.application.AuthApplicationService;
import product.orders.authservice.application.dto.LoginRequest;
import product.orders.authservice.application.dto.LoginResponse;
import product.orders.authservice.application.dto.RegisterUserRequest;

import java.util.UUID;

/**
 * Controller for handling authentication-related operations.
 * <p>
 * This class provides endpoints for user authentication and registration.
 * It delegates the actual business logic to the {@link AuthApplicationService}.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        String token = authApplicationService.authenticate(request);
        return new LoginResponse(token);
    }

    @PostMapping("/register")
    public UUID register(@RequestBody @Valid RegisterUserRequest request) {
        return authApplicationService.registerUser(request);
    }
}
