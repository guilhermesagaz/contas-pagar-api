package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.dto.ContaDto;
import com.br.contasapagar.application.exception.NotFoundException;
import com.br.contasapagar.domain.enumeration.EnvioArquivoStatusEnum;
import com.br.contasapagar.domain.model.Conta;
import com.br.contasapagar.domain.model.EnvioArquivo;
import com.br.contasapagar.domain.repository.ContaRepository;
import com.br.contasapagar.domain.repository.EnvioArquivoRepository;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import com.br.contasapagar.presentation.exception.ImportacaoCsvException;
import com.br.contasapagar.shared.util.FileUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.br.contasapagar.utils.ContaTestUtils.createConta;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ContaImportacaoCsvServiceTest {

    @InjectMocks
    private ContaImportacaoCsvService service;

    @Mock
    private ContaRepository repository;

    @Mock
    private EnvioArquivoRepository envioArquivoRepository;

    @Mock
    private MessageHandler messageHandler;

    @Mock
    private Validator validator;

    @Mock
    private MultipartFile multipartFile;

    private EnvioArquivo envioArquivo;

    @BeforeEach
    void setUp() {
        envioArquivo = EnvioArquivo.builder()
                .descricao("test.csv")
                .arquivo(new byte[]{})
                .status(EnvioArquivoStatusEnum.EM_ANDAMENTO)
                .dataEnvio(LocalDateTime.now())
                .build();
    }

    @Test
    void validarFormatoArquivoFormatoInvalido() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("arquivo",
                "contas.pdf", "application/pdf", new ByteArrayInputStream("dummy data".getBytes()));

        assertThrows(ImportacaoCsvException.class, () -> service.validarFormatoArquivo(mockFile));
    }

    @Test
    void validarFormatoArquivoFormatoValido() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("arquivo",
                "contas.csv", "text/csv", new ByteArrayInputStream("dummy data".getBytes()));

        assertDoesNotThrow(() -> service.validarFormatoArquivo(mockFile));
    }

    @Test
    void findEnvioArquivoSucesso() {
        when(envioArquivoRepository.findById(anyLong())).thenReturn(Optional.of(envioArquivo));

        EnvioArquivo foundEnvioArquivo = service.findEnvioArquivo(1L);

        assertNotNull(foundEnvioArquivo);
        assertEquals(envioArquivo.getDescricao(), foundEnvioArquivo.getDescricao());
    }

    @Test
    void findEnvioArquivoNotFound() {
        when(envioArquivoRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(messageHandler.getMessage(anyString(), anyString())).thenReturn("Envio Arquivo não encontrado");

        assertThrows(NotFoundException.class, () -> service.findEnvioArquivo(1L));
    }

    @Test
    void saveEnvioArquivoSucesso() throws IOException {
        when(multipartFile.getName()).thenReturn("test.csv");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));
        when(envioArquivoRepository.save(any())).thenReturn(envioArquivo);

        EnvioArquivo savedEnvioArquivo = service.saveEnvioArquivo(multipartFile);

        assertNotNull(savedEnvioArquivo);
        assertEquals(envioArquivo.getDescricao(), savedEnvioArquivo.getDescricao());
        assertEquals(EnvioArquivoStatusEnum.EM_ANDAMENTO, savedEnvioArquivo.getStatus());
    }

    @Test
    void setConcluidoEnvioArquivo() {
        service.setConcluidoEnvioArquivo(envioArquivo);

        assertEquals(EnvioArquivoStatusEnum.CONCLUIDO, envioArquivo.getStatus());
        assertNotNull(envioArquivo.getDataConclusao());
        verify(envioArquivoRepository, times(1)).save(envioArquivo);
    }

    @Test
    void setErroEnvioArquivo() {
        when(envioArquivoRepository.findById(anyLong())).thenReturn(Optional.of(envioArquivo));

        service.setErroEnvioArquivo(1L, "Erro ao processar o arquivo");

        assertEquals(EnvioArquivoStatusEnum.ERRO, envioArquivo.getStatus());
        assertEquals("Erro ao processar o arquivo", envioArquivo.getMensagem());
        verify(envioArquivoRepository, times(1)).save(envioArquivo);
    }

    @Test
    void salvarDadosSucesso() {
        List<Conta> contas = List.of(createConta()); // Simular contas
        when(repository.saveAll(anyList())).thenReturn(contas);

        List<Conta> result = service.salvarDados(contas);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).saveAll(anyList());
    }
}
