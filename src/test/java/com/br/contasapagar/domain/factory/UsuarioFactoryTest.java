package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.usuario.UsuarioResponse;
import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.domain.service.PerfilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.br.contasapagar.utils.PerfilTestUtils.*;
import static com.br.contasapagar.utils.UsuarioTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UsuarioFactoryTest {

    @InjectMocks
    private UsuarioFactory factory;

    @Mock
    private PerfilService perfilService;

    @Mock
    private PerfilFactory perfilFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testToEntity() {
        when(perfilService.findById(any())).thenReturn(createPerfil());
        when(passwordEncoder.encode(any())).thenReturn(USUARIO_PASSWORD);

        Usuario usuario = factory.toEntity(createUsuarioRequest());

        assertEquals(USUARIO_NOME, usuario.getNome());
        assertEquals(USUARIO_USERNAME, usuario.getUsername());
        assertEquals(USUARIO_PASSWORD, usuario.getPassword());
        assertEquals(PERFIL_ID, usuario.getPerfis().get(0).getId());
        assertEquals(PERFIL_NOME, usuario.getPerfis().get(0).getNome());
        assertFalse(usuario.isExpirado());
        assertFalse(usuario.isBloqueado());
        assertFalse(usuario.isCredenciaisExpiradas());
        assertTrue(usuario.isAtivo());
    }

    @Test
    public void testToResponse() {
        when(perfilFactory.toResponse(any())).thenReturn(createPerfilResponse());

        UsuarioResponse response = factory.toResponse(createUsuario());

        assertEquals(USUARIO_ID, response.getId());
        assertEquals(USUARIO_NOME, response.getNome());
        assertEquals(USUARIO_USERNAME, response.getUsername());
        assertEquals(PERFIL_ID, response.getPerfis().get(0).getId());
        assertEquals(PERFIL_NOME, response.getPerfis().get(0).getNome());
    }
}
