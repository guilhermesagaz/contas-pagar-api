package com.br.contasapagar.presentation;

import com.br.contasapagar.application.exception.handler.ApiExceptionHandler;
import com.br.contasapagar.application.filter.JwtTokenVerifierFilter;
import com.br.contasapagar.application.payload.perfil.PerfilRequest;
import com.br.contasapagar.application.payload.perfil.PerfilResponse;
import com.br.contasapagar.domain.factory.PerfilFactory;
import com.br.contasapagar.domain.model.Perfil;
import com.br.contasapagar.domain.service.PerfilService;
import com.br.contasapagar.presentation.controller.PerfilController;
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

import static com.br.contasapagar.utils.PerfilTestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PerfilController.class)
public class PerfilControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ApiExceptionHandler apiExceptionHandler;

    @MockBean
    private JwtTokenVerifierFilter jwtTokenVerifierFilter;

    @MockBean
    private PerfilService perfilService;

    @MockBean
    private PerfilFactory perfilFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private PerfilResponse perfilResponse;
    private PerfilRequest perfilRequest;
    private Page<Perfil> perfilPage;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        perfilResponse = createPerfilResponse();
        perfilRequest = createPerfilRequest();
        perfilPage = new PageImpl<>(List.of(createPerfil()));

        when(perfilService.findAll(any())).thenReturn(perfilPage);
        when(perfilService.findById(any())).thenReturn(createPerfil());
        when(perfilFactory.toResponse(any())).thenReturn(perfilResponse);
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindAllPagedSucesso() throws Exception {
        mockMvc.perform(get("/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(perfilResponse.getId()))
                .andExpect(jsonPath("$.content[0].tipo").value(perfilResponse.getTipo().name()))
                .andExpect(jsonPath("$.content[0].nome").value(perfilResponse.getNome()))
                .andExpect(jsonPath("$.content[0].descricao").value(perfilResponse.getDescricao()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindByIdSucesso() throws Exception {
        mockMvc.perform(get("/perfil/{id}", PERFIL_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(perfilResponse.getId()))
                .andExpect(jsonPath("$.tipo").value(perfilResponse.getTipo().name()))
                .andExpect(jsonPath("$.nome").value(perfilResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(perfilResponse.getDescricao()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testSaveSucesso() throws Exception {
        when(perfilService.save(any())).thenReturn(createPerfil());
        when(perfilFactory.toEntity(any())).thenReturn(createPerfil());

        mockMvc.perform(post("/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfilRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(perfilResponse.getId()))
                .andExpect(jsonPath("$.tipo").value(perfilResponse.getTipo().name()))
                .andExpect(jsonPath("$.nome").value(perfilResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(perfilResponse.getDescricao()));
    }
}
