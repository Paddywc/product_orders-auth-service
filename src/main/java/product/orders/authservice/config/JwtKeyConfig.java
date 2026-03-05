package product.orders.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;

/**
 * Configuration class for handling JWT (JSON Web Token) keystore properties.
 * <p>
 * This class reads keystore-related properties that are prefixed with "jwt.keystore"
 * from the application configuration and provides a Spring bean for a {@link KeyPair}.
 * The {@link KeyPair} is derived from the keystore file specified by the configured properties.
 * <p>
 * Properties include:
 * - `location`: Path to the keystore file on the classpath.
 * - `password`: Password for accessing the keystore.
 * - `alias`: Alias of the key within the keystore.
 * <p>
 * A {@link KeyPair} consists of a public key and private key, which are essential components
 * for signing and verifying JWTs in the application.
 * <p>
 * The class ensures the proper loading of the keystore and retrieval of the key material,
 * throwing appropriate exceptions if the keys cannot be loaded correctly.
 */
@Configuration
@ConfigurationProperties(prefix = "jwt.keystore")
public class JwtKeyConfig {

    private String location;
    private String password;
    private String alias;

    @Bean
    public KeyPair keyPair() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream is = new ClassPathResource(location.replace("classpath:", "")).getInputStream()) {
            keyStore.load(is, password.toCharArray());
        }

        Key key = keyStore.getKey(alias, password.toCharArray());
        if (!(key instanceof PrivateKey privateKey)) {
            throw new IllegalStateException("No private key found");
        }

        Certificate cert = keyStore.getCertificate(alias);
        PublicKey publicKey = cert.getPublicKey();

        return new KeyPair(publicKey, privateKey);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
