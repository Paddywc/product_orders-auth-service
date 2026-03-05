package product.orders.authservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import product.orders.authservice.domain.exception.InvalidCredentialsException;
import product.orders.authservice.domain.exception.UserInactiveException;
import product.orders.authservice.domain.security.PasswordMatcher;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserTest {

    private UserId userId;
    private EmailAddress email;
    private PasswordHash passwordHash;

    @BeforeEach
    void setUp() {
        userId = UserId.newId();
        email = new EmailAddress("user@example.com");
        passwordHash = new PasswordHash("hashed-password");
    }

    @Test
    void testConstruct_ValidData_InitializeWithPendingStatus() {
        // Arrange
        User user = new User(
                userId,
                email,
                passwordHash,
                Set.of(Role.USER)
        );

        // Act & Assert
        assertThatThrownBy(user::ensureActive)
                .isInstanceOf(UserInactiveException.class);
    }

    @Test
    void testConstruct_PassedRoles_CopiesRolesDefensively() {
        // Arrange
        Set<Role> roles = Set.of(Role.USER);

        // Act
        User user = new User(userId, email, passwordHash, roles);

        // Assert
        assertThat(user.getRoles()).containsExactly(Role.USER);
        assertThat(user.getRoles()).isNotSameAs(roles);
    }

    @Test
    void testActivate_ValidUser_SetsStatusToActive() {
        // Arrange
        User user = new User(userId, email, passwordHash, Set.of(Role.USER));
        // Act
        user.activate();
        // Assert
        assertThatCode(user::ensureActive).doesNotThrowAnyException();
    }

    @Test
    void testEnsureActive_UserNotActive_ThrowsException() {
        // Arrange
        User user = new User(userId, email, passwordHash, Set.of(Role.USER));
        // Act + Assert
        assertThatThrownBy(user::ensureActive)
                .isInstanceOf(UserInactiveException.class);
    }

    @Test
    void testEnsureActive_UserActive_DoesNotThrowException() {
        // Arrange
        User user = new User(userId, email, passwordHash, Set.of(Role.USER));
        user.activate();
        // Act + Assert
        assertThatCode(user::ensureActive).doesNotThrowAnyException();
    }


    @Test
    void testVerifyPassword_PasswordMatches_DoesNotThrowException() {
        // Arrange
        User user = new User(userId, email, passwordHash, Set.of(Role.USER));

        PasswordMatcher passwordMatcher = mock(PasswordMatcher.class);
        when(passwordMatcher.matches("raw", "hashed-password"))
                .thenReturn(true);

        // Act + Assert
        assertThatCode(() ->
                user.verifyPassword("raw", passwordMatcher)
        ).doesNotThrowAnyException();

        verify(passwordMatcher).matches("raw", "hashed-password");
    }

    @Test
    void testVerifyPassword_PasswordDoesNotMatch_ThrowsException(){
        // Arrange
        User user = new User(userId, email, passwordHash, Set.of(Role.USER));

        PasswordMatcher matcher = mock(PasswordMatcher.class);
        when(matcher.matches("wrong", "hashed-password")).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() ->
                user.verifyPassword("wrong", matcher)
        ).isInstanceOf(InvalidCredentialsException.class);
    }
}
