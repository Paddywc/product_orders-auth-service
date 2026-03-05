package product.orders.authservice.infrastructure.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import product.orders.authservice.domain.model.EmailAddress;

/**
 * Converter for persisting EmailAddress value objects in the database.
 */
@Converter(autoApply = false)
public class EmailAddressConverter implements AttributeConverter<EmailAddress, String> {
    @Override
    public String convertToDatabaseColumn(EmailAddress attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.value();
    }

    @Override
    public EmailAddress convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new EmailAddress(dbData);
    }
}
