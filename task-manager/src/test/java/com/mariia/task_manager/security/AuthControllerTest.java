package com.mariia.task_manager.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariia.task_manager.controller.AuthController;
import com.mariia.task_manager.model.User;
import com.mariia.task_manager.payload.LoginRequest;
import com.mariia.task_manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testUserLoginSuccess() throws Exception{
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password123"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        String loginRequest = objectMapper.writeValueAsString(
                new LoginRequest("testuser", "password123")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk()) // Then: Login should succeed
                .andExpect(jsonPath("$.token").exists()); // Ensure token is in response
    }


    @Test
    public void testUserLoginFailure() throws Exception{
        when(userRepository.findByUsername("wronguser")).thenReturn(java.util.Optional.empty());

        String loginRequest = objectMapper.writeValueAsString(
                new LoginRequest("wronguser", "wrongpassword")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
            .andExpect(status().isUnauthorized());

    }
}
