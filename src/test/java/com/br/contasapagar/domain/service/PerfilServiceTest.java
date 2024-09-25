package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.exception.NotFoundException;
import com.br.contasapagar.domain.model.Perfil;
import com.br.contasapagar.domain.repository.PerfilRepository;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import com.br.contasapagar.presentation.exception.NomePerfilExistenteException;
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

import static com.br.contasapagar.utils.PerfilTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PerfilServiceTest {

    @InjectMocks
    private PerfilService service;

    @Mock
    private PerfilRepository repository;

    @Mock
    private MessageHandler messageHandler;

    @Test
    public void testFindAllSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Perfil> page = new PageImpl<>(List.of(createPerfil()));
        when(repository.findAll(pageable)).thenReturn(page);

        Page<Perfil> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(PERFIL_ID, result.getContent().get(0).getId());
        assertEquals(PERFIL_TIPO, result.getContent().get(0).getTipo());
        assertEquals(PERFIL_NOME, result.getContent().get(0).getNome());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindByIdSuccess() {
        when(repository.findById(PERFIL_ID)).thenReturn(Optional.of(createPerfil()));

        Perfil result = service.findById(PERFIL_ID);

        assertNotNull(result);
        assertEquals(PERFIL_ID, result.getId());
        assertEquals(PERFIL_TIPO, result.getTipo());
        assertEquals(PERFIL_NOME, result.getNome());
        verify(repository, times(1)).findById(PERFIL_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        when(repository.findById(PERFIL_ID)).thenReturn(Optional.empty());
        when(messageHandler.getMessage(any(), any())).thenReturn("Perfil não encontrado");

        assertThrows(NotFoundException.class, () -> service.findById(PERFIL_ID));

        verify(repository, times(1)).findById(PERFIL_ID);
    }

    @Test
    public void testSaveSuccess() {
        when(repository.existsByNome(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(createPerfil());

        Perfil result = service.save(createPerfil());

        assertNotNull(result);
        assertEquals(PERFIL_ID, result.getId());
        assertEquals(PERFIL_TIPO, result.getTipo());
        assertEquals(PERFIL_NOME, result.getNome());
        verify(repository, times(1)).existsByNome(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    public void testSaveNomeExistente() {
        when(repository.existsByNome(any())).thenReturn(true);
        when(messageHandler.getMessage(any(), any())).thenReturn("Nome de perfil já existente");

        assertThrows(NomePerfilExistenteException.class, () -> service.save(createPerfil()));

        verify(repository, times(1)).existsByNome(any());
        verify(repository, times(0)).save(any());
    }
}
