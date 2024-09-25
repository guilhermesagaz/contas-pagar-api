package com.br.contasapagar.domain.model;

import com.br.contasapagar.domain.enumeration.EnvioArquivoStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnvioArquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private EnvioArquivoStatusEnum status;

//    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] arquivo;

    private String mensagem;

    private LocalDateTime dataEnvio;

    private LocalDateTime dataConclusao;
}
