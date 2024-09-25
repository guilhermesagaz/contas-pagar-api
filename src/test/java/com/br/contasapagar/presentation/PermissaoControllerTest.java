package com.br.contasapagar.presentation;

import com.br.contasapagar.application.exception.handler.ApiExceptionHandler;
import com.br.contasapagar.application.filter.JwtTokenVerifierFilter;
import com.br.contasapagar.application.payload.permissao.PermissaoResponse;
import com.br.contasapagar.domain.factory.PermissaoFactory;
import com.br.contasapagar.domain.model.Permissao;
import com.br.contasapagar.domain.service.PermissaoService;
import com.br.contasapagar.presentation.controller.PermissaoController;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.br.contasapagar.utils.PermissaoTestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PermissaoController.class)
public class PermissaoControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ApiExceptionHandler apiExceptionHandler;

    @MockBean
    private JwtTokenVerifierFilter jwtTokenVerifierFilter;

    @MockBean
    private PermissaoService permissaoService;

    @MockBean
    private PermissaoFactory permissaoFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private PermissaoResponse permissaoResponse;
    private Page<Permissao> permissaoPage;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        permissaoResponse = createPermissaoResponse();
        permissaoPage = new PageImpl<>(List.of(createPermissao()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindAllPagedSucesso() throws Exception {
        when(permissaoService.findAll(any())).thenReturn(permissaoPage);
        when(permissaoFactory.toResponse(any())).thenReturn(permissaoResponse);

        mockMvc.perform(get("/permissao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(permissaoResponse.getId()))
                .andExpect(jsonPath("$.content[0].nome").value(permissaoResponse.getNome()))
                .andExpect(jsonPath("$.content[0].descricao").value(permissaoResponse.getDescricao()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindByIdSucesso() throws Exception {
        when(permissaoService.findById(any())).thenReturn(createPermissao());
        when(permissaoFactory.toResponse(any())).thenReturn(permissaoResponse);

        mockMvc.perform(get("/permissao/{id}", PERMISSAO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(permissaoResponse.getId()))
                .andExpect(jsonPath("$.nome").value(permissaoResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(permissaoResponse.getDescricao()));
    }
}
