package product.orders.authservice.infrastructure.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import product.orders.authservice.domain.model.PasswordHash;

/**
 * Converter for persisting PasswordHash value objects in the database.
 */
@Converter(autoApply = false)
public class PasswordHashConverter implements AttributeConverter<PasswordHash, String> {
    @Override
    public String convertToDatabaseColumn(PasswordHash attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.value();
    }

    @Override
    public PasswordHash convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new PasswordHash(dbData);
    }
}
