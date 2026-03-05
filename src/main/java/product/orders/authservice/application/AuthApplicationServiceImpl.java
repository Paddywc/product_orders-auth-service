package product.orders.authservice.application;

import org.springframework.stereotype.Service;
import product.orders.authservice.domain.exception.EmailAlreadyExistsException;
import product.orders.authservice.domain.exception.InvalidCredentialsException;
import product.orders.authservice.domain.model.*;
import product.orders.authservice.domain.security.PasswordHasher;
import product.orders.authservice.domain.security.PasswordMatcher;
import product.orders.authservice.infrastructure.repository.UserRepository;
import product.orders.authservice.application.dto.LoginRequest;
import product.orders.authservice.application.dto.RegisterUserRequest;
import product.orders.authservice.domain.model.JwtSubject;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthApplicationServiceImpl implements AuthApplicationService {
    private final UserRepository userRepository;
    private final PasswordMatcher passwordMatcher;
    private final JwtIssuer jwtIssuer;

    private final PasswordHasher passwordHasher;

    public AuthApplicationServiceImpl(UserRepository userRepository,
                                      PasswordMatcher passwordMatcher,
                                      JwtIssuer jwtIssuer,
                                      PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordMatcher = passwordMatcher;
        this.jwtIssuer = jwtIssuer;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public String authenticate(LoginRequest command) {

        User user = userRepository.findByEmail(new EmailAddress(command.email()));
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        user.ensureActive();                 // domain rule
        user.verifyPassword(
                command.password(),
                passwordMatcher
        );

        Set<String> roles = user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.toSet());
        JwtSubject subject = new JwtSubject(
                user.getId().value(),
                user.getEmail().value(),
                roles
        );

        return jwtIssuer.issueToken(subject);
    }

    /**
     * Register new user and save it to the database.
     *
     * @param command user email address and raw password
     * @return id of the registered user
     */
    @Override
    public UUID registerUser(RegisterUserRequest command) throws EmailAlreadyExistsException {
        EmailAddress address = new EmailAddress(command.email());
        if (userRepository.existsByEmail(address)) {
            throw new EmailAlreadyExistsException();
        }

        String hashedPassword = passwordHasher.hash(command.rawPassword());
        UserId userId = UserId.newId();
        User user = new User(
                userId,
                address,
                new PasswordHash(hashedPassword),
                Set.of(Role.USER)
        );
        user.activate();

        userRepository.save(user);

        return user.getId().value();
    }
}
