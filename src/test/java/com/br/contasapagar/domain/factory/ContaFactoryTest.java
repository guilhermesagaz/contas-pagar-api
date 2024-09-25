package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.conta.ContaResponse;
import com.br.contasapagar.application.payload.conta.ValorTotalResponse;
import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import com.br.contasapagar.domain.model.Conta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.br.contasapagar.utils.ContaTestUtils.*;
import static com.br.contasapagar.utils.ContaTestUtils.createContaRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
public class ContaFactoryTest {

    @InjectMocks
    private ContaFactory factory;

    @Test
    void testToEntity() {
        Conta conta = factory.toEntity(createContaRequest());

        assertEquals(CONTA_VALOR, conta.getValor());
        assertEquals(CONTA_DESCRICAO, conta.getDescricao());
        assertEquals(CONTA_SITUACAO, conta.getSituacao());
        assertEquals(CONTA_DATA_VENCIMENTO, conta.getDataVencimento());
        assertNull(conta.getDataPagamento());
    }

    @Test
    void testToEntityFromSituacaoRequest() {
        Conta conta = factory.toEntity(createSituacaoRequest());

        assertEquals(SituacaoEnum.PAGO, conta.getSituacao());
        assertEquals(LocalDate.now(), conta.getDataPagamento());
    }

    @Test
    void testToResponse() {
        ContaResponse response = factory.toResponse(createConta());

        assertEquals(CONTA_ID, response.getId());
        assertEquals(CONTA_VALOR, response.getValor());
        assertEquals(CONTA_DESCRICAO, response.getDescricao());
        assertEquals(CONTA_SITUACAO, response.getSituacao());
        assertEquals(CONTA_DATA_VENCIMENTO, response.getDataVencimento());
        assertNull(response.getDataPagamento());
    }

    @Test
    void testToValorTotalResponse() {
        ValorTotalResponse response = factory.toValorTotalResponse(CONTA_VALOR);

        assertEquals(CONTA_VALOR, response.getValorTotal());
    }
}
