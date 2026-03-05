package product.orders.authservice.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import product.orders.authservice.domain.security.PasswordHasher;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BCryptPasswordHasherTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testHash_validRawPassword_returnsEncodedValue() {
        // arrange
        String rawPassword = "my-password";
        String encodedPassword = "encoded-password";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        PasswordHasher hasher = new BCryptPasswordHasher(passwordEncoder);

        // act
        String result = hasher.hash(rawPassword);

        // assert
        assertThat(result).isEqualTo(encodedPassword);
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void testHash_nullRawPassword_delegatesToPasswordEncoder() {
        // arrange
        String rawPassword = null;
        when(passwordEncoder.encode(null)).thenReturn("encoded-null");

        PasswordHasher hasher = new BCryptPasswordHasher(passwordEncoder);

        // act
        String result = hasher.hash(rawPassword);

        // assert
        assertThat(result).isEqualTo("encoded-null");
        verify(passwordEncoder).encode(null);
    }

    @Test
    void testHash_passwordEncoderThrowsException_propagatesException() {
        // arrange
        String rawPassword = "password";
        when(passwordEncoder.encode(rawPassword))
                .thenThrow(new RuntimeException("encoding failed"));

        PasswordHasher hasher = new BCryptPasswordHasher(passwordEncoder);

        // act & assert
        assertThatThrownBy(() ->
                hasher.hash(rawPassword)
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("encoding failed");

        verify(passwordEncoder).encode(rawPassword);
    }
}