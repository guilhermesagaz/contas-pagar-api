package com.br.contasapagar.application.payload.envioArquivo;

import com.br.contasapagar.domain.enumeration.EnvioArquivoStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EnvioArquivoResponse {

    private Long id;
    private String descricao;
    private EnvioArquivoStatusEnum status;
    private String mensagem;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataConclusao;
}
