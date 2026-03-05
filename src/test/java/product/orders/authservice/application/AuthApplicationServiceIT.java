package product.orders.authservice.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import product.orders.authservice.application.dto.LoginRequest;
import product.orders.authservice.application.dto.RegisterUserRequest;
import product.orders.authservice.domain.model.*;
import product.orders.authservice.domain.security.PasswordHasher;
import product.orders.authservice.infrastructure.repository.UserRepository;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class AuthApplicationServiceIT {
    @Container
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("inv_test_db")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private AuthApplicationService authApplicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    void testAuthenticate_UserExists_ReturnsToken() {
        // Arrange
        String email = "test@example.co.uk";
        String unhashedPassword = "testPassword123!";
        String hashedPassword = passwordHasher.hash(unhashedPassword);
        UserId userId = UserId.newId();
        User user = new User(
                userId,
                new EmailAddress(email),
                new PasswordHash(hashedPassword),
                Set.of(Role.USER)
        );
        user.activate();
        userRepository.saveAndFlush(user);

        // Act
        LoginRequest request = new LoginRequest(email, unhashedPassword);
        String token = authApplicationService.authenticate(request);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testRegisterUser_UniqueUserEmail_PersistsUser(){
        // Arrange
        String email = "unique@example.co.uk";
        String unhashedPassword = "testPassword123!";
        RegisterUserRequest request = new RegisterUserRequest(email, unhashedPassword);

        // Act
        UUID userId = authApplicationService.registerUser(request);

        // Assert
        assertNotNull(userId);
        User user = userRepository.findByEmail(new EmailAddress(email));
        assertNotNull(user);
        assertEquals(email, user.getEmail().toString());
    }

}
