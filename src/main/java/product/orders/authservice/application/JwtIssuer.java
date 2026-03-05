package product.orders.authservice.application;

import product.orders.authservice.domain.model.JwtSubject;

public interface JwtIssuer {
    String issueToken(JwtSubject subject);
}
