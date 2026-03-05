package product.orders.authservice.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import product.orders.authservice.domain.security.PasswordMatcher;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BCryptPasswordMatcherTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testMatches_validCredentials_returnsTrue() {
        // arrange
        String rawPassword = "password";
        String passwordHash = "encoded";
        when(passwordEncoder.matches(rawPassword, passwordHash))
                .thenReturn(true);

        PasswordMatcher matcher = new BCryptPasswordMatcher(passwordEncoder);

        // act
        boolean result = matcher.matches(rawPassword, passwordHash);

        // assert
        assertThat(result).isTrue();
        verify(passwordEncoder).matches(rawPassword, passwordHash);
    }

    @Test
    void testMatches_invalidCredentials_returnsFalse() {
        // arrange
        String rawPassword = "wrong";
        String passwordHash = "encoded";
        when(passwordEncoder.matches(rawPassword, passwordHash))
                .thenReturn(false);

        PasswordMatcher matcher = new BCryptPasswordMatcher(passwordEncoder);

        // act
        boolean result = matcher.matches(rawPassword, passwordHash);

        // assert
        assertThat(result).isFalse();
        verify(passwordEncoder).matches(rawPassword, passwordHash);
    }

    @Test
    void testMatches_passwordEncoderThrowsException_propagatesException() {
        // arrange
        String rawPassword = "password";
        String passwordHash = "encoded";
        when(passwordEncoder.matches(rawPassword, passwordHash))
                .thenThrow(new RuntimeException("match failed"));

        PasswordMatcher matcher = new BCryptPasswordMatcher(passwordEncoder);

        // act & assert
        assertThatThrownBy(() ->
                matcher.matches(rawPassword, passwordHash)
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("match failed");

        verify(passwordEncoder).matches(rawPassword, passwordHash);
    }
}