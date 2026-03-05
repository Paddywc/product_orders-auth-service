package product.orders.authservice.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller for handling JSON Web Key (JWK) operations.
 * <p>
 * This class provides an endpoint for retrieving the JWK set in JSON format.
 * The JWK set is used for verifying JWT signatures in the application.
 */
@RestController
public class JwkController {


    private final JWKSet jwkSet;

    public JwkController(JWKSet jwkSet) {
        this.jwkSet = jwkSet;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {
        return jwkSet.toJSONObject();
    }
}
