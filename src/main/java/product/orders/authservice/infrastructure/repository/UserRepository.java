package product.orders.authservice.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.orders.authservice.domain.model.EmailAddress;
import product.orders.authservice.domain.model.User;
import product.orders.authservice.domain.model.UserId;

interface UserRepository extends JpaRepository<User, UserId> {

    User findByEmail(EmailAddress email);
}
