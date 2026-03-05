package product.orders.authservice.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import product.orders.authservice.application.dto.LoginRequest;
import product.orders.authservice.application.dto.RegisterUserRequest;
import product.orders.authservice.domain.exception.EmailAlreadyExistsException;
import product.orders.authservice.domain.exception.InvalidCredentialsException;
import product.orders.authservice.domain.model.*;
import product.orders.authservice.domain.security.PasswordHasher;
import product.orders.authservice.domain.security.PasswordMatcher;
import product.orders.authservice.infrastructure.repository.UserRepository;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthApplicationServiceImplTest {

    private UserRepository userRepository;
    private PasswordMatcher passwordMatcher;
    private PasswordHasher passwordHasher;
    private JwtIssuer jwtIssuer;

    private AuthApplicationServiceImpl service;


    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordMatcher = mock(PasswordMatcher.class);
        passwordHasher = mock(PasswordHasher.class);
        jwtIssuer = mock(JwtIssuer.class);

        service = new AuthApplicationServiceImpl(
                userRepository,
                passwordMatcher,
                jwtIssuer,
                passwordHasher
        );
    }


    @Test
    void testAuthenticate_ValidCredentials_ReturnsToken() {
        // Arrange
        String email = "test@example.com";
        String password = "secret";
        UserId userId = UserId.newId();

        User user = new User(
                userId,
                new EmailAddress(email),
                new PasswordHash("hashed"),
                Set.of(Role.USER)
        );
        user.activate();

        when(userRepository.findByEmail(new EmailAddress(email)))
                .thenReturn(user);

        when(jwtIssuer.issueToken(any())).thenReturn("jwt-token");
        when(passwordMatcher.matches(any(), any())).thenReturn(true);

        ArgumentCaptor<JwtSubject> captor = ArgumentCaptor.forClass(JwtSubject.class);

        // Act
        String token = service.authenticate(
                new LoginRequest(email, password)
        );

        // Assert
        assertThat(token).isEqualTo("jwt-token");

        verify(jwtIssuer).issueToken(captor.capture());
        JwtSubject subject = captor.getValue();
        assertThat(subject.userId()).isEqualTo(userId.value());
        assertThat(subject.email()).isEqualTo(email);
    }

    @Test
    void testAuthenticate_UserNotFound_ThrowsInvalidCredentials() {
        // Arrange
        when(userRepository.findByEmail(any()))
                .thenReturn(null);

        // Act + Assert
        assertThatThrownBy(() ->
                service.authenticate(new LoginRequest("x@test.com", "pw"))
        ).isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void testAuthenticate_InactiveUser_throwsException() {
        // Arrange
        EmailAddress email = new EmailAddress("test@example.com");
        PasswordHash passwordHash = new PasswordHash("hashed");
        User user = new User(
                UserId.newId(),
                email,
                passwordHash,
                Set.of(Role.USER)
        ); // not activated

        when(userRepository.findByEmail(email)).thenReturn(user);

        LoginRequest request = new LoginRequest("test@example.com", "password");

        // Act + Assert
        assertThatThrownBy(() -> service.authenticate(request))
                .isInstanceOf(RuntimeException.class); // depends on ensureActive()
    }

    @Test
    void testAuthenticate_InvalidPassword_ThrowsException() {
        // Arrange
        EmailAddress email = new EmailAddress("test@example.com");
        PasswordHash passwordHash = new PasswordHash("hashed");
        User user = new User(
                UserId.newId(),
                email,
                passwordHash,
                Set.of(Role.USER)
        );
        user.activate();
        when(userRepository.findByEmail(email)).thenReturn(user);

        doThrow(new InvalidCredentialsException())
                .when(passwordMatcher)
                .matches(any(), any());

        LoginRequest request = new LoginRequest("test@example.com", "wrong");

        // Act + Assert
        assertThatThrownBy(() -> service.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void testRegisterUser_ValidRequest_SavesUserAndReturnsId() {
        // Arrange
        String email = "new@example.com";
        String rawPassword = "password";
        String hashed = "hashed-password";

        when(userRepository.existsByEmail(new EmailAddress(email)))
                .thenReturn(false);

        when(passwordHasher.hash(rawPassword))
                .thenReturn(hashed);

        // Act
        UUID result = service.registerUser(
                new RegisterUserRequest(email, rawPassword)
        );

        // Assert
        assertThat(result).isNotNull();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertThat(savedUser.getEmail().value()).isEqualTo(email);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists_ThrowsException() {
        // Arrange
        String email = "test@example.com";

        when(userRepository.existsByEmail(new EmailAddress(email)))
                .thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() ->
                service.registerUser(
                        new RegisterUserRequest(email, "pw")
                )
        ).isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void testRegisterUser_PassedValidData_HashesPasswordBeforeSaving() {
        // Arrange
        when(userRepository.existsByEmail(any()))
                .thenReturn(false);

        when(passwordHasher.hash("pw"))
                .thenReturn("hashed");

        // Act
        service.registerUser(
                new RegisterUserRequest("a@test.com", "pw")
        );

        // Assert
        verify(passwordHasher).hash("pw");
    }
}
