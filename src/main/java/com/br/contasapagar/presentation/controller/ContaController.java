package com.br.contasapagar.presentation.controller;

import com.br.contasapagar.application.payload.conta.ContaRequest;
import com.br.contasapagar.application.payload.conta.ContaResponse;
import com.br.contasapagar.application.payload.conta.SituacaoRequest;
import com.br.contasapagar.application.payload.conta.ValorTotalResponse;
import com.br.contasapagar.application.payload.envioArquivo.EnvioArquivoResponse;
import com.br.contasapagar.domain.factory.ContaFactory;
import com.br.contasapagar.domain.model.EnvioArquivo;
import com.br.contasapagar.domain.service.ContaImportacaoCsvService;
import com.br.contasapagar.domain.service.ContaService;
import com.br.contasapagar.domain.service.RabbitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

import static com.br.contasapagar.shared.constant.Constants.ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("conta")
public class ContaController {

    private final ContaService service;
    private final ContaFactory factory;
    private final ContaImportacaoCsvService importacaoCsvService;
    private final RabbitService rabbitService;

    @GetMapping
    public ResponseEntity<Page<ContaResponse>> findAllPaged(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) LocalDate dataVencimento,
            @PageableDefault(sort = ID, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findAllPaged(descricao, dataVencimento, pageable).map(factory::toResponse));
    }

    @GetMapping("{id}")
    public ResponseEntity<ContaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(factory.toResponse(service.findById(id)));
    }

    @GetMapping("envio_arquivo/{id}")
    public ResponseEntity<EnvioArquivoResponse> findEnvioArquivo(@PathVariable Long id) {
        return ResponseEntity.ok(factory.toEnvioArquivoResponse(importacaoCsvService.findEnvioArquivo(id)));
    }

    @GetMapping("valor_total_pago")
    public ResponseEntity<ValorTotalResponse> findValorTotal(@RequestParam LocalDate dataInicial,
                                                             @RequestParam LocalDate dataFinal) {
        return ResponseEntity.ok(factory.toValorTotalResponse(service.sumTotalPagoPorPeriodo(dataInicial, dataFinal)));
    }

    @PostMapping
    public ResponseEntity<ContaResponse>  save(@RequestBody @Valid ContaRequest request) {
        ContaResponse response = factory.toResponse(service.save(factory.toEntity(request)));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("{id}")
                .buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<ContaResponse> update(@PathVariable Long id, @RequestBody @Valid ContaRequest request) {
        return ResponseEntity.ok(factory.toResponse(service.update(id, factory.toEntity(request))));
    }

    @PatchMapping("{id}/situacao")
    public ResponseEntity<ContaResponse> updateSituacao(@PathVariable Long id,
                                                        @RequestBody @Valid SituacaoRequest request) {
        return ResponseEntity.ok(factory.toResponse(service.updateSituacao(id, factory.toEntity(request))));
    }

    @PostMapping(value = "importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnvioArquivoResponse> importar(@RequestParam("arquivo") MultipartFile file) throws IOException {
        importacaoCsvService.validarFormatoArquivo(file);
        EnvioArquivo envioArquivo = importacaoCsvService.saveEnvioArquivo(file);

        rabbitService.enviarContasImportacao(envioArquivo.getId());

        return ResponseEntity.ok(factory.toEnvioArquivoResponse(envioArquivo));
    }
}
