package product.orders.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import product.orders.authservice.domain.exception.EmailAlreadyExistsException;
import product.orders.authservice.domain.exception.InvalidCredentialsException;
import product.orders.authservice.domain.exception.UserInactiveException;

/**
 * Controller advice for handling exceptions in the authentication controller.
 * <p>
 * This class provides centralized exception handling for the {@link AuthController}.
 * It maps specific exception types to HTTP responses, ensuring consistent error handling
 * across the authentication endpoints.
 */
@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthControllerAdvice {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Void> invalidCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<Void> userInactive() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Void> emailAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
