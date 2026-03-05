package product.orders.authservice.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EmailAddressTest {
    // ==========================================
    // VALID EMAILS
    // ==========================================

    @Test
    void shouldAcceptStandardEmail() {
        EmailAddress email = new EmailAddress("user@example.com");

        assertThat(email.value()).isEqualTo("user@example.com");
    }

    @Test
    void shouldAcceptSubdomainEmail() {
        EmailAddress email = new EmailAddress("john.doe@company.co.uk");

        assertThat(email.value()).isEqualTo("john.doe@company.co.uk");
    }

    @Test
    void shouldAcceptPlusTagEmail() {
        EmailAddress email = new EmailAddress("user+tag@gmail.com");

        assertThat(email.value()).isEqualTo("user+tag@gmail.com");
    }

    @Test
    void shouldAcceptUnderscoreAndDash() {
        EmailAddress email = new EmailAddress("a_b-c@sub.domain.io");

        assertThat(email.value()).isEqualTo("a_b-c@sub.domain.io");
    }

    @Test
    void toString_shouldReturnValue() {
        EmailAddress email = new EmailAddress("test@example.com");

        assertThat(email.toString()).isEqualTo("test@example.com");
    }

    @Test
    void equals_shouldWorkForSameValue() {
        EmailAddress e1 = new EmailAddress("a@b.com");
        EmailAddress e2 = new EmailAddress("a@b.com");

        assertThat(e1).isEqualTo(e2);
        assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
    }

    // ==========================================
    // INVALID EMAILS
    // ==========================================

    @Test
    void shouldThrowWhenNull() {
        assertThatThrownBy(() -> new EmailAddress(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email address cannot be null");
    }

    @Test
    void shouldRejectMissingAt() {
        assertThatThrownBy(() -> new EmailAddress("invalid-email"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email address format");
    }

    @Test
    void shouldRejectMissingDomain() {
        assertThatThrownBy(() -> new EmailAddress("user@"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email address format");
    }

    @Test
    void shouldRejectMissingTld() {
        assertThatThrownBy(() -> new EmailAddress("user@domain"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email address format");
    }

    @Test
    void shouldRejectDoubleAt() {
        assertThatThrownBy(() -> new EmailAddress("user@@domain.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email address format");
    }

    @Test
    void shouldRejectLeadingDotInDomain() {
        assertThatThrownBy(() -> new EmailAddress("user@.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email address format");
    }

    @Test
    void shouldRejectWhitespace() {
        assertThatThrownBy(() -> new EmailAddress("user @example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email address format");
    }
}
