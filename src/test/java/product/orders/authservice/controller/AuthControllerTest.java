package product.orders.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import product.orders.authservice.application.AuthApplicationService;
import product.orders.authservice.application.dto.LoginRequest;
import product.orders.authservice.application.dto.RegisterUserRequest;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthApplicationService authApplicationService;

    @Test
    void testLogin_ValidRequest_ReturnsToken() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest(
                "user@example.com",
                "password"
        );

        when(authApplicationService.authenticate(request))
                .thenReturn("jwt-token");

        // Act + Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(authApplicationService).authenticate(request);
    }

    @Test
    void testLogin_InvalidRequest_Returns400() throws Exception {
        // Missing fields (invalid request)
        String invalidJson = "{}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_ValidRequest_ReturnsUserId() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        RegisterUserRequest request =
                new RegisterUserRequest("new@example.com", "password");

        when(authApplicationService.registerUser(request))
                .thenReturn(userId);

        // Act + Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                        .andExpect(content().string("\"" + userId + "\""));
        verify(authApplicationService).registerUser(request);
    }

    @Test
    void testRegister_InvalidRequest_Returns400() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}