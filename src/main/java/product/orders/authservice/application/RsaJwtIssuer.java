package product.orders.authservice.application;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import product.orders.authservice.domain.model.JwtSubject;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Service
public class RsaJwtIssuer implements JwtIssuer {

    private final KeyPair keyPair;
    private final String issuer;
    private final Duration ttl;

    public RsaJwtIssuer(KeyPair keyPair,
                        @Value("${jwt.issuer}") String issuer,
                        @Value("${jwt.expiration-seconds}") long ttlSeconds) {
        this.keyPair = keyPair;
        this.issuer = issuer;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    @Override
    public String issueToken(JwtSubject subject) {

        Instant now = Instant.now();
        Instant expires = now.plus(ttl);

        Set<String> roles = subject.roles() == null
                ? Set.of()
                : subject.roles();

        return Jwts.builder()
                .subject(subject.userId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expires))
                .claim("email", subject.email())
                .claim("roles", roles)
                .signWith(keyPair.getPrivate())
                .compact();
    }
}
