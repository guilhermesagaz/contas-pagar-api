package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.dto.ContaDto;
import com.br.contasapagar.application.payload.conta.ContaRequest;
import com.br.contasapagar.application.payload.conta.ContaResponse;
import com.br.contasapagar.application.payload.conta.SituacaoRequest;
import com.br.contasapagar.application.payload.conta.ValorTotalResponse;
import com.br.contasapagar.application.payload.envioArquivo.EnvioArquivoResponse;
import com.br.contasapagar.domain.model.Conta;
import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import com.br.contasapagar.domain.model.EnvioArquivo;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ContaFactory {

    public Conta toEntity(ContaRequest request) {
        return Conta.builder()
                .valor(request.getValor())
                .descricao(request.getDescricao())
                .situacao(request.getDataVencimento().isBefore(LocalDate.now()) ?
                        SituacaoEnum.ATRASADA : SituacaoEnum.PENDENTE)
                .dataVencimento(request.getDataVencimento())
                .build();
    }

    public Conta toEntity(SituacaoRequest request) {
        return Conta.builder()
                .situacao(request.getSituacao())
                .dataPagamento(SituacaoEnum.PAGO.equals(request.getSituacao()) ? LocalDate.now() : null)
                .build();
    }

    public Conta toEntityFromDto(ContaDto dto) {
        return Conta.builder()
                .valor(dto.getValor())
                .descricao(dto.getDescricao())
                .situacao(dto.getSituacao())
                .dataVencimento(dto.getDataVencimento())
                .dataPagamento(dto.getDataPagamento())
                .build();
    }

    public List<Conta> toEntityList(List<ContaDto> dtos) {
        if (ObjectUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(this::toEntityFromDto)
                .collect(Collectors.toList());
    }

    public ContaResponse toResponse(Conta entity) {
        return ContaResponse.builder()
                .id(entity.getId())
                .valor(entity.getValor())
                .descricao(entity.getDescricao())
                .situacao(entity.getSituacao())
                .dataVencimento(entity.getDataVencimento())
                .dataPagamento(entity.getDataPagamento())
                .build();
    }

    public ValorTotalResponse toValorTotalResponse(BigDecimal valorTotal) {
        return ValorTotalResponse.builder()
                .valorTotal(valorTotal)
                .build();
    }

    public EnvioArquivoResponse toEnvioArquivoResponse(EnvioArquivo envioArquivo) {
        return EnvioArquivoResponse.builder()
                .id(envioArquivo.getId())
                .descricao(envioArquivo.getDescricao())
                .status(envioArquivo.getStatus())
                .mensagem(envioArquivo.getMensagem())
                .dataEnvio(envioArquivo.getDataEnvio())
                .dataConclusao(envioArquivo.getDataConclusao())
                .build();
    }
}
