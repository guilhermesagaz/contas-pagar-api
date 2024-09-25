package com.br.contasapagar.domain.service;

import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.domain.repository.UsuarioRepository;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.br.contasapagar.utils.UsuarioTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private MessageHandler messageHandler;

    @Test
    public void testFindAllSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> page = new PageImpl<>(List.of(createUsuario()));
        when(repository.findAll(pageable)).thenReturn(page);

        Page<Usuario> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(USUARIO_ID, result.getContent().get(0).getId());
        assertEquals(USUARIO_NOME, result.getContent().get(0).getNome());
        assertEquals(USUARIO_USERNAME, result.getContent().get(0).getUsername());
        assertEquals(USUARIO_PASSWORD, result.getContent().get(0).getPassword());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindByUsernameSuccess() {
        when(repository.findByUsername(USUARIO_USERNAME)).thenReturn(Optional.of(createUsuario()));

        Usuario result = service.findByUsername(USUARIO_USERNAME);

        assertNotNull(result);
        assertEquals(USUARIO_USERNAME, result.getUsername());
        verify(repository, times(1)).findByUsername(USUARIO_USERNAME);
    }

    @Test
    public void testFindByUsernameNotFound() {
        when(repository.findByUsername(USUARIO_USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.findByUsername(USUARIO_USERNAME));

        verify(repository, times(1)).findByUsername(USUARIO_USERNAME);
    }

    @Test
    public void testSaveSuccess() {
        when(repository.existsByUsername(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(createUsuario());

        Usuario result = service.save(createUsuario());

        assertNotNull(result);
        assertEquals(USUARIO_ID, result.getId());
        assertEquals(USUARIO_NOME, result.getNome());
        assertEquals(USUARIO_USERNAME, result.getUsername());
        verify(repository, times(1)).existsByUsername(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    public void testSaveUsernameExists() {
        Usuario usuario = createUsuario();
        when(repository.existsByUsername(USUARIO_USERNAME)).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> service.save(usuario));

        verify(repository, times(1)).existsByUsername(USUARIO_USERNAME);
        verify(repository, times(0)).save(usuario);
    }
}
