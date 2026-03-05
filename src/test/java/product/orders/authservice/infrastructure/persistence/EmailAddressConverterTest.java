package product.orders.authservice.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import product.orders.authservice.domain.model.EmailAddress;

import static org.assertj.core.api.Assertions.*;

class EmailAddressConverterTest {

    private final EmailAddressConverter converter = new EmailAddressConverter();

    @Test
    void testConvertToDatabaseColumn_validEmail_returnsStringValue() {
        // arrange
        EmailAddress email = new EmailAddress("user@example.com");

        // act
        String result = converter.convertToDatabaseColumn(email);

        // assert
        assertThat(result).isEqualTo("user@example.com");
    }

    @Test
    void testConvertToDatabaseColumn_null_returnsNull() {
        // arrange
        EmailAddress email = null;

        // act
        String result = converter.convertToDatabaseColumn(email);

        // assert
        assertThat(result).isNull();
    }

    @Test
    void testConvertToEntityAttribute_validString_returnsEmailAddress() {
        // arrange
        String dbValue = "user@example.com";

        // act
        EmailAddress result = converter.convertToEntityAttribute(dbValue);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("user@example.com");
    }

    @Test
    void testConvertToEntityAttribute_null_returnsNull() {
        // arrange
        String dbValue = null;

        // act
        EmailAddress result = converter.convertToEntityAttribute(dbValue);

        // assert
        assertThat(result).isNull();
    }

    @Test
    void testConvertToEntityAttribute_invalidEmail_throwsException() {
        // arrange
        String invalidValue = "invalid-email";

        // act & assert
        assertThatThrownBy(() ->
                converter.convertToEntityAttribute(invalidValue)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}