package com.br.contasapagar.presentation;

import com.br.contasapagar.application.exception.handler.ApiExceptionHandler;
import com.br.contasapagar.application.filter.JwtTokenVerifierFilter;
import com.br.contasapagar.application.payload.usuario.UsuarioRequest;
import com.br.contasapagar.application.payload.usuario.UsuarioResponse;
import com.br.contasapagar.domain.factory.UsuarioFactory;
import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.domain.service.UsuarioService;
import com.br.contasapagar.presentation.controller.UsuarioController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.br.contasapagar.utils.UsuarioTestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ApiExceptionHandler apiExceptionHandler;

    @MockBean
    private JwtTokenVerifierFilter jwtTokenVerifierFilter;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioFactory usuarioFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        usuarioRequest = createUsuarioRequest();
        usuario = createUsuario();
        usuarioResponse = createUsuarioResponse();
    }

    @Test
    void testSaveSucesso() throws Exception {
        when(usuarioFactory.toEntity(any(UsuarioRequest.class))).thenReturn(usuario);
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioFactory.toResponse(any(Usuario.class))).thenReturn(usuarioResponse);

        mockMvc.perform(post("/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioResponse.getId()))
                .andExpect(jsonPath("$.nome").value(usuarioResponse.getNome()))
                .andExpect(jsonPath("$.username").value(usuarioResponse.getUsername()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindAllSucesso() throws Exception {
        Page<Usuario> usuarios = new PageImpl<>(List.of(usuario));

        when(usuarioService.findAll(any())).thenReturn(usuarios);
        when(usuarioFactory.toResponse(any())).thenReturn(usuarioResponse);

        mockMvc.perform(get("/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(usuarioResponse.getId()))
                .andExpect(jsonPath("$.content[0].nome").value(usuarioResponse.getNome()))
                .andExpect(jsonPath("$.content[0].username").value(usuarioResponse.getUsername()));
    }
}
