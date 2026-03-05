package product.orders.authservice.infrastructure.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import product.orders.authservice.domain.model.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Container
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void overrideDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    void testFindByEmail_UserExists_ReturnsUser() {
        // Arrange
        String email = "test@example.com";
        UserId userId = UserId.newId();
        User user = new User(
                userId,
                new EmailAddress(email),
                new PasswordHash("hashedpass"),
                Set.of(Role.USER)
        );
        userRepository.saveAndFlush(user);

        // Act
        User foundUser = userRepository.findByEmail(new EmailAddress(email));

        // Assert
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail().toString());
        assertEquals(userId.value(), foundUser.getId().value());
    }

    @Test
    void testExistsByEmail_UserExists_ReturnsTrue() {
        // Arrange
        EmailAddress email = new EmailAddress("test@example.com");
        UserId userId = UserId.newId();
        User user = new User(
                userId,
                email,
                new PasswordHash("hashedpass"),
                Set.of(Role.USER)
        );
        userRepository.saveAndFlush(user);

        // Act & Assert
        assertTrue(userRepository.existsByEmail(email));
    }

    @Test
    void testExistsByEmail_UserDoesNotExist_ReturnsFalse() {
        EmailAddress email = new EmailAddress("missing@example.com");

        assertFalse(userRepository.existsByEmail(email));
    }

    @Test
    void testSave_DuplicateEmail_ThrowsException() {
        // Arrange
        EmailAddress duplicate = new EmailAddress("duplicate@email.com");

        User userOne = new User(
                UserId.newId(),
                duplicate,
                new PasswordHash("hashedpass"),
                Set.of(Role.USER));
        User userTwo = new User(
                UserId.newId(),
                duplicate,
                new PasswordHash("hashedpass"),
                Set.of(Role.USER)
        );
        userRepository.saveAndFlush(userOne);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(userTwo));
    }

}

