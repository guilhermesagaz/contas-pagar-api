package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.exception.NotFoundException;
import com.br.contasapagar.domain.model.Permissao;
import com.br.contasapagar.domain.repository.PermissaoRepository;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.br.contasapagar.utils.PermissaoTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PermissaoServiceTest {

    @InjectMocks
    private PermissaoService service;

    @Mock
    private PermissaoRepository repository;

    @Mock
    private MessageHandler messageHandler;

    @Test
    public void testFindAllSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Permissao> page = new PageImpl<>(List.of(createPermissao()));

        when(repository.findAll(pageable)).thenReturn(page);

        Page<Permissao> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(PERMISSAO_ID, result.getContent().get(0).getId());
        assertEquals(PERMISSAO_NOME, result.getContent().get(0).getNome());
        assertEquals(PERMISSAO_DESCRICAO, result.getContent().get(0).getDescricao());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindByIdSuccess() {
        when(repository.findById(PERMISSAO_ID)).thenReturn(Optional.of(createPermissao()));

        Permissao result = service.findById(PERMISSAO_ID);

        assertNotNull(result);
        assertEquals(PERMISSAO_ID, result.getId());
        assertEquals(PERMISSAO_NOME, result.getNome());
        assertEquals(PERMISSAO_DESCRICAO, result.getDescricao());
        verify(repository, times(1)).findById(PERMISSAO_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        when(repository.findById(PERMISSAO_ID)).thenReturn(Optional.empty());
        when(messageHandler.getMessage(any(), any())).thenReturn("Permissão não encontrada");

        assertThrows(NotFoundException.class, () -> service.findById(PERMISSAO_ID));

        verify(repository, times(1)).findById(PERMISSAO_ID);
    }
}
