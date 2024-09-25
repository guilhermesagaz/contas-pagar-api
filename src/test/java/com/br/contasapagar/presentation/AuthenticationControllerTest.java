package com.br.contasapagar.presentation;

import com.br.contasapagar.application.exception.handler.ApiExceptionHandler;
import com.br.contasapagar.application.filter.JwtTokenVerifierFilter;
import com.br.contasapagar.application.payload.auth.AuthenticationRequest;
import com.br.contasapagar.application.payload.auth.AuthenticationResponse;
import com.br.contasapagar.application.payload.auth.RefreshTokenRequest;
import com.br.contasapagar.domain.service.AuthenticationService;
import com.br.contasapagar.presentation.controller.AuthenticationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ApiExceptionHandler apiExceptionHandler;

    @MockBean
    private JwtTokenVerifierFilter jwtTokenVerifierFilter;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;
    private RefreshTokenRequest refreshTokenRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        authenticationRequest = AuthenticationRequest.builder()
                .username("usuario.teste@teste.com")
                .password("password")
                .build();

        authenticationResponse = AuthenticationResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken("refresh-token")
                .build();
    }

    @Test
    void testLoginSucesso() throws Exception {
        when(authenticationService.login(anyString(), anyString())).thenReturn(authenticationResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authenticationResponse.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationResponse.getRefreshToken()));
    }

    @Test
    void testRefreshTokenSucesso() throws Exception {
        when(authenticationService.refreshToken(anyString())).thenReturn(authenticationResponse);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authenticationResponse.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationResponse.getRefreshToken()));
    }

    @Test
    @WithMockUser
    void testLogoutSucesso() throws Exception {
        doNothing().when(authenticationService).logout(anyString());

        mockMvc.perform(post("/auth/logout")
                        .principal(() -> authenticationRequest.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
