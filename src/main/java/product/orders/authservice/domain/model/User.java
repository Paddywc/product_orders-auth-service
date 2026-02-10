package product.orders.authservice.domain.model;

import product.orders.authservice.domain.exception.UserInactiveException;

import java.util.Set;

public class User {

    private final UserId id;
    private EmailAddress email;
    private PasswordHash passwordHash;
    private Set<Role> roles;
    private UserStatus status;

    public User(UserId id,
                EmailAddress email,
                PasswordHash passwordHash,
                Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.status = UserStatus.PENDING;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.DISABLED;
    }

    public boolean canAuthenticate() {
        return status == UserStatus.ACTIVE;
    }

    public void ensureActive() {
        if (status != UserStatus.ACTIVE) {
            throw new UserInactiveException(id.value());
        }
    }

}
