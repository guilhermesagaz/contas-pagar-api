package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.exception.NotFoundException;
import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import com.br.contasapagar.domain.model.Conta;
import com.br.contasapagar.domain.repository.ContaRepository;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import com.br.contasapagar.presentation.exception.ContaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.br.contasapagar.utils.ContaTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository repository;

    @Mock
    private MessageHandler messageHandler;

    @Test
    public void testFindByIdSuccess() {
        when(repository.findById(CONTA_ID)).thenReturn(Optional.of(createConta()));

        Conta result = contaService.findById(CONTA_ID);

        assertEquals(CONTA_ID, result.getId());
        assertEquals(CONTA_VALOR, result.getValor());
        assertEquals(CONTA_DESCRICAO, result.getDescricao());
        assertEquals(CONTA_SITUACAO, result.getSituacao());
        assertEquals(CONTA_SITUACAO, result.getSituacao());
        assertEquals(CONTA_DATA_VENCIMENTO, result.getDataVencimento());
        assertNull(result.getDataPagamento());
        verify(repository).findById(CONTA_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        when(repository.findById(CONTA_ID)).thenReturn(Optional.empty());
        when(messageHandler.getMessage(anyString(), anyString())).thenReturn("Conta não encontrada");

        assertThrows(NotFoundException.class, () -> contaService.findById(CONTA_ID));
    }

    @Test
    public void testFindAllPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> contaPage = new PageImpl<>(List.of(createConta()));
        when(repository.findAllPaged(null, null, pageable)).thenReturn(contaPage);

        Page<Conta> result = contaService.findAllPaged(null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(CONTA_ID, result.getContent().get(0).getId());
        assertEquals(CONTA_VALOR, result.getContent().get(0).getValor());
        assertEquals(CONTA_DESCRICAO, result.getContent().get(0).getDescricao());
        verify(repository).findAllPaged(null, null, pageable);
    }

    @Test
    public void testSumTotalPagoPorPeriodoSuccess() {
        LocalDate dataInicial = LocalDate.of(2023, 1, 1);
        LocalDate dataFinal = LocalDate.of(2023, 12, 31);

        when(repository.sumValorByDataPagamentoBetween(dataInicial, dataFinal)).thenReturn(CONTA_VALOR);

        BigDecimal result = contaService.sumTotalPagoPorPeriodo(dataInicial, dataFinal);

        assertEquals(CONTA_VALOR, result);
        verify(repository).sumValorByDataPagamentoBetween(dataInicial, dataFinal);
    }

    @Test
    public void testSumTotalPagoPorPeriodoInvalidPeriod() {
        LocalDate dataInicial = LocalDate.of(2024, 1, 1);
        LocalDate dataFinal = LocalDate.of(2023, 12, 31);
        when(messageHandler.getMessage(anyString())).thenReturn("Data inicial não pode ser maior que a data final");

        assertThrows(ContaException.class, () -> contaService.sumTotalPagoPorPeriodo(dataInicial, dataFinal));
    }

    @Test
    public void testSave() {
        Conta conta = createConta();
        when(repository.save(any())).thenReturn(conta);

        Conta result = contaService.save(conta);

        assertEquals(conta, result);
        verify(repository, times(1)).save(conta);
    }

    @Test
    public void testUpdateSuccess() {
        Conta contaToUpdate = createConta();
        contaToUpdate.setValor(BigDecimal.valueOf(2000));

        when(repository.findById(CONTA_ID)).thenReturn(Optional.of(createConta()));
        when(repository.save(contaToUpdate)).thenReturn(contaToUpdate);

        Conta result = contaService.update(CONTA_ID, contaToUpdate);

        assertEquals(CONTA_ID, result.getId());
        assertEquals(contaToUpdate.getValor(), result.getValor());
    }

    @Test
    public void testUpdateSituacaoSuccess() {
        Conta contaToUpdate = createConta();
        contaToUpdate.setSituacao(SituacaoEnum.PAGO);
        contaToUpdate.setDataPagamento(LocalDate.now());

        when(repository.findById(CONTA_ID)).thenReturn(Optional.of(createConta()));
        when(repository.save(any())).thenReturn(contaToUpdate);

        Conta result = contaService.updateSituacao(CONTA_ID, contaToUpdate);

        assertEquals(SituacaoEnum.PAGO, result.getSituacao());
        assertEquals(LocalDate.now(), result.getDataPagamento());
    }

    @Test
    public void testAlterarSituacaoContasAtrasadas() {
        Conta conta = createConta();
        conta.setDataVencimento(LocalDate.now().minusDays(1));

        when(repository.findAllAtrasadosNaData(any(LocalDate.class))).thenReturn(List.of(conta));

        contaService.alterarSituacaoContasAtrasadas();

        verify(repository, times(1)).saveAll(List.of(conta));
        assertEquals(SituacaoEnum.ATRASADA, conta.getSituacao());
    }
}
