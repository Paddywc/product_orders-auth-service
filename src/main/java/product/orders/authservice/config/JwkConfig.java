package product.orders.authservice.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Configuration class responsible for providing the JSON Web Key Set (JWKSet) used for
 * JWT (JSON Web Token) signing and verification.
 * <p>
 * This class defines a Spring bean for JWKSet which is derived from an RSAKey built using
 * the provided KeyPair and a unique key identifier.
 * <p>
 * The `jwkSet` method combines the RSA public and private keys along with the key ID into
 * a JWK representation and exposes it as a JWKSet bean, which is accessible across the
 * Spring application context.
 */
@Configuration
public class JwkConfig {

    @Bean
    public JWKSet jwkSet(KeyPair keyPair,
                         @Value("${jwt.key-id}") String keyId) {

        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(keyId)
                .build();

        return new JWKSet(rsaKey.toPublicJWK());
    }
}
