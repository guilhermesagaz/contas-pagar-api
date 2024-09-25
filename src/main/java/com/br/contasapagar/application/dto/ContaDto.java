package com.br.contasapagar.application.dto;

import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import com.br.contasapagar.shared.converter.LocalDateConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaDto {

    @CsvBindByName(column = "VALOR", required = true)
    @NotNull(message = "O campo valor é obrigatório e deve ser informado")
    @Digits(integer = 15, fraction = 2, message = "O campo valor deve ter no máximo 15 dígitos e 2 casas decimais")
    private BigDecimal valor;

    @CsvBindByName(column = "DESCRICAO")
    @Size(max = 400, message = "O tamanho do campo descrição deve ser menor ou igual à {max}")
    private String descricao;

    @CsvBindByName(column = "SITUACAO", required = true)
    @NotNull(message = "O campo situação é obrigatório e deve ser informado")
    private SituacaoEnum situacao;

    @CsvCustomBindByName(column = "DATA VENCIMENTO", converter = LocalDateConverter.class, required = true)
    @NotNull(message = "O campo data de vencimento é obrigatório e deve ser informado")
    private LocalDate dataVencimento;

    @CsvCustomBindByName(column = "DATA PAGAMENTO", converter = LocalDateConverter.class)
    private LocalDate dataPagamento;
}
