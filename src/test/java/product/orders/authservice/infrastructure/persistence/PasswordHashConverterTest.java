package product.orders.authservice.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import product.orders.authservice.domain.model.PasswordHash;

import static org.assertj.core.api.Assertions.*;

class PasswordHashConverterTest {

    private final PasswordHashConverter converter = new PasswordHashConverter();

    @Test
    void testConvertToDatabaseColumn_validPasswordHash_returnsStringValue() {
        // arrange
        PasswordHash passwordHash = new PasswordHash("hashed-password");

        // act
        String result = converter.convertToDatabaseColumn(passwordHash);

        // assert
        assertThat(result).isEqualTo("hashed-password");
    }

    @Test
    void testConvertToDatabaseColumn_null_returnsNull() {
        // arrange
        PasswordHash passwordHash = null;

        // act
        String result = converter.convertToDatabaseColumn(passwordHash);

        // assert
        assertThat(result).isNull();
    }

    @Test
    void testConvertToEntityAttribute_validString_returnsPasswordHash() {
        // arrange
        String dbValue = "hashed-password";

        // act
        PasswordHash result = converter.convertToEntityAttribute(dbValue);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("hashed-password");
    }

    @Test
    void testConvertToEntityAttribute_null_returnsNull() {
        // arrange
        String dbValue = null;

        // act
        PasswordHash result = converter.convertToEntityAttribute(dbValue);

        // assert
        assertThat(result).isNull();
    }
}