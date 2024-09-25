package com.br.contasapagar.presentation;

import com.br.contasapagar.application.exception.handler.ApiExceptionHandler;
import com.br.contasapagar.application.filter.JwtTokenVerifierFilter;
import com.br.contasapagar.application.payload.conta.ContaRequest;
import com.br.contasapagar.application.payload.conta.ContaResponse;
import com.br.contasapagar.application.payload.conta.SituacaoRequest;
import com.br.contasapagar.application.payload.conta.ValorTotalResponse;
import com.br.contasapagar.application.payload.envioArquivo.EnvioArquivoResponse;
import com.br.contasapagar.domain.enumeration.EnvioArquivoStatusEnum;
import com.br.contasapagar.domain.factory.ContaFactory;
import com.br.contasapagar.domain.model.Conta;
import com.br.contasapagar.domain.model.EnvioArquivo;
import com.br.contasapagar.domain.service.ContaImportacaoCsvService;
import com.br.contasapagar.domain.service.ContaService;
import com.br.contasapagar.domain.service.RabbitService;
import com.br.contasapagar.presentation.controller.ContaController;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.br.contasapagar.utils.ContaTestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ApiExceptionHandler apiExceptionHandler;

    @MockBean
    private JwtTokenVerifierFilter jwtTokenVerifierFilter;

    @MockBean
    private ContaService contaService;

    @MockBean
    private ContaFactory contaFactory;

    @MockBean
    private ContaImportacaoCsvService contaImportacaoCsvService;

    @MockBean
    private RabbitService rabbitService;

    @Autowired
    private ObjectMapper objectMapper;

    private ContaResponse contaResponse;
    private ContaRequest contaRequest;
    private Page<Conta> contaPage;
    private EnvioArquivoResponse envioArquivoResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        contaResponse = createContaResponse();
        contaRequest = createContaRequest();
        contaPage = new PageImpl<>(List.of(createConta()));
        envioArquivoResponse = EnvioArquivoResponse.builder()
                .id(1L)
                .status(EnvioArquivoStatusEnum.EM_ANDAMENTO)
                .build();

        when(contaService.findAllPaged(any(), any(), any())).thenReturn(contaPage);
        when(contaService.findById(any())).thenReturn(createConta());
        when(contaService.save(any())).thenReturn(createConta());
        when(contaService.update(anyLong(), any(Conta.class))).thenReturn(createConta());
        when(contaFactory.toResponse(any())).thenReturn(contaResponse);
        when(contaFactory.toEntity(any(ContaRequest.class))).thenReturn(createConta());
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindAllPaged_sucesso() throws Exception {
        mockMvc.perform(get("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.content[0].valor").value(contaResponse.getValor()))
                .andExpect(jsonPath("$.content[0].descricao").value(contaResponse.getDescricao()))
                .andExpect(jsonPath("$.content[0].situacao").value(contaResponse.getSituacao().name()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindByIdSucesso() throws Exception {
        mockMvc.perform(get("/conta/{id}", CONTA_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.valor").value(contaResponse.getValor()))
                .andExpect(jsonPath("$.descricao").value(contaResponse.getDescricao()))
                .andExpect(jsonPath("$.situacao").value(contaResponse.getSituacao().name()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testSaveSucesso() throws Exception {
        mockMvc.perform(post("/conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.valor").value(contaResponse.getValor()))
                .andExpect(jsonPath("$.descricao").value(contaResponse.getDescricao()))
                .andExpect(jsonPath("$.situacao").value(contaResponse.getSituacao().name()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testUpdateSucesso() throws Exception {
        mockMvc.perform(put("/conta/{id}", CONTA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.valor").value(contaResponse.getValor()))
                .andExpect(jsonPath("$.descricao").value(contaResponse.getDescricao()))
                .andExpect(jsonPath("$.situacao").value(contaResponse.getSituacao().name()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testUpdateSituacaoSucesso() throws Exception {
        SituacaoRequest situacaoRequest = createSituacaoRequest();
        when(contaService.updateSituacao(anyLong(), any(Conta.class))).thenReturn(createConta());

        mockMvc.perform(patch("/conta/{id}/situacao", CONTA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(situacaoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.valor").value(contaResponse.getValor()))
                .andExpect(jsonPath("$.descricao").value(contaResponse.getDescricao()))
                .andExpect(jsonPath("$.situacao").value(contaResponse.getSituacao().name()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testImportarSucesso() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("arquivo",
                "contas.csv", "text/csv", new ByteArrayInputStream("dummy data".getBytes()));

        when(contaImportacaoCsvService.saveEnvioArquivo(any())).thenReturn(new EnvioArquivo());
        when(contaFactory.toEnvioArquivoResponse(any())).thenReturn(envioArquivoResponse);

        mockMvc.perform(multipart("/conta/importar")
                        .file(mockFile))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindEnvioArquivoSucesso() throws Exception {
        when(contaImportacaoCsvService.findEnvioArquivo(anyLong())).thenReturn(new EnvioArquivo());
        when(contaFactory.toEnvioArquivoResponse(any())).thenReturn(envioArquivoResponse);

        mockMvc.perform(get("/conta/envio_arquivo/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(envioArquivoResponse.getId()))
                .andExpect(jsonPath("$.status").value(envioArquivoResponse.getStatus().name()));
    }

    @Test
    @WithMockUser(authorities = "usuario:admin")
    void testFindValorTotalSucesso() throws Exception {
        ValorTotalResponse response = createValorTotalResponse();
        when(contaService.sumTotalPagoPorPeriodo(any(), any())).thenReturn(CONTA_VALOR);
        when(contaFactory.toValorTotalResponse(any())).thenReturn(response);

        mockMvc.perform(get("/conta/valor_total_pago")
                        .param("dataInicial", "2024-01-01")
                        .param("dataFinal", "2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal").value(response.getValorTotal()));
    }
}
