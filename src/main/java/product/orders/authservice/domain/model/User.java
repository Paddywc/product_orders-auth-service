package product.orders.authservice.domain.model;

import jakarta.persistence.*;
import product.orders.authservice.domain.exception.InvalidCredentialsException;
import product.orders.authservice.domain.exception.UserInactiveException;
import product.orders.authservice.domain.security.PasswordMatcher;
import product.orders.authservice.infrastructure.persistence.EmailAddressConverter;
import product.orders.authservice.infrastructure.persistence.PasswordHashConverter;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity representing how a user is stored in the database and its business logic.
 */
@Entity
@Table(name = "auth_users")
public class User {

    @Id
    @Embedded
    private UserId id;

    @Convert(converter = EmailAddressConverter.class)
    @Column(name = "email", nullable = false, unique = true)
    private EmailAddress email;

    @Convert(converter = PasswordHashConverter.class)
    @Column(name = "password_hash", nullable = false)
    private PasswordHash passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus status;

    protected User() {
        // for JPA
    }

    public User(UserId id,
                EmailAddress email,
                PasswordHash passwordHash,
                Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        if (roles != null) {
            this.roles = new HashSet<>(roles);
        }
        this.status = UserStatus.PENDING;
    }

    /**
     * Sets the user status to active.
     */
    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    /**
     * Throws {@link UserInactiveException} if the user is inactive.
     */
    public void ensureActive() {
        if (status != UserStatus.ACTIVE) {
            throw new UserInactiveException(id.value());
        }
    }

    /**
     * Throws {@link InvalidCredentialsException} if the raw password does not match the stored hash.
     * @param rawPassword the raw password to be verified
     * @param passwordMatcher the password matcher to be used for verification
     */
    public void verifyPassword(String rawPassword, PasswordMatcher passwordMatcher) {
        if (!passwordMatcher.matches(rawPassword, passwordHash.value())) {
            throw new InvalidCredentialsException();
        }
    }

    public UserId getId() {
        return id;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

}
