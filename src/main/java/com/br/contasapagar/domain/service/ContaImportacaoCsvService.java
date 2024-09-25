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
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.br.contasapagar.shared.constant.Constants.*;

@RequiredArgsConstructor
@Service
public class ContaImportacaoCsvService {

    private static final char SEPARATOR = ';';
    private static final String VIRGULA_ESPACO = ", ";
    private static final String PONTO_ESPACO = ". ";

    private final ContaRepository repository;
    private final EnvioArquivoRepository envioArquivoRepository;
    private final MessageHandler messageHandler;
    private final Validator validator;

    public void validarFormatoArquivo(MultipartFile file) {
        if (FileUtil.isNotCsv(file)) {
            throw new ImportacaoCsvException(messageHandler.getMessage(ARQUIVO_FORMATO_INVALIDO_EXCEPTION));
        }
    }

    public EnvioArquivo findEnvioArquivo(Long id) {
        return envioArquivoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHandler.getMessage(NOT_FOUND, "Envio Arquivo")));
    }

    @Transactional
    public EnvioArquivo saveEnvioArquivo(MultipartFile file) throws IOException {
        EnvioArquivo envioArquivo = EnvioArquivo.builder()
                .descricao(file.getName())
                .arquivo(file.getInputStream().readAllBytes())
                .status(EnvioArquivoStatusEnum.EM_ANDAMENTO)
                .dataEnvio(LocalDateTime.now())
                .build();

        return envioArquivoRepository.save(envioArquivo);
    }

    @Transactional
    public void setConcluidoEnvioArquivo(EnvioArquivo envioArquivo){
        envioArquivo.setStatus(EnvioArquivoStatusEnum.CONCLUIDO);
        envioArquivo.setDataConclusao(LocalDateTime.now());

        envioArquivoRepository.save(envioArquivo);
    }

    @Transactional
    public void setErroEnvioArquivo(Long envioArquivoId, String mensagemErro){
        EnvioArquivo envioArquivo = findEnvioArquivo(envioArquivoId);

        envioArquivo.setStatus(EnvioArquivoStatusEnum.ERRO);
        envioArquivo.setMensagem(mensagemErro);

        envioArquivoRepository.save(envioArquivo);
    }

    public List<ContaDto> lerValidarArquivo(EnvioArquivo envioArquivo) {
        List<ContaDto> contaDtos = readCSVtoDto(envioArquivo);

        validarDtos(contaDtos);

        return contaDtos;
    }

    @Transactional
    public List<Conta> salvarDados(List<Conta> contasImpostadas) {
        return repository.saveAll(contasImpostadas);
    }

    private void validarDtos(List<ContaDto> dtos) {
        List<String> errosValidacao = IntStream.range(0, dtos.size())
                .parallel()
                .mapToObj(i -> validarErros(dtos.get(i), i + 1))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (!errosValidacao.isEmpty()) {
            throw new ImportacaoCsvException(String.join(VIRGULA_ESPACO, errosValidacao));
        }
    }

    private List<ContaDto> readCSVtoDto(EnvioArquivo envioArquivo) {
        try {
            CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(
                    new ByteArrayInputStream(envioArquivo.getArquivo()), StandardCharsets.ISO_8859_1))
                    .withCSVParser(new CSVParserBuilder()
                            .withSeparator(SEPARATOR)
                            .build())
                    .build();

            CsvToBean<ContaDto> buildCsv = new CsvToBeanBuilder<ContaDto>(csvReader)
                    .withType(ContaDto.class)
                    .withIgnoreQuotations(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();

            return buildCsv.parse();
        } catch (Exception e) {
            throw new ImportacaoCsvException(messageHandler.getMessage(ERRO_LEITURA_CSV_EXCEPTION));
        }
    }

    private Optional<String> validarErros(ContaDto dto, int count) {
        Set<ConstraintViolation<ContaDto>> violations = validator.validate(dto);

        if (violations.isEmpty()) {
            return Optional.empty();
        }

        String mensagensErro = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(PONTO_ESPACO));

        return Optional.of(getMensagemValidacao(String.valueOf(count), mensagensErro));
    }

    private String getMensagemValidacao(String linha, String mensagem) {
        return messageHandler.getMessage(VALIDACAO_LINHA_CSV_EXCEPTION, linha, mensagem);
    }
}
