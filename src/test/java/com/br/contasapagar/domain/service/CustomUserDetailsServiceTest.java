package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.payload.auth.CustomUserDetails;
import com.br.contasapagar.domain.factory.AuthFactory;
import com.br.contasapagar.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.br.contasapagar.utils.AuthTestUtils.createCustomUserDetails;
import static com.br.contasapagar.utils.UsuarioTestUtils.USUARIO_USERNAME;
import static com.br.contasapagar.utils.UsuarioTestUtils.createUsuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService service;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AuthFactory authFactory;

    private Usuario mockUsuario;
    private CustomUserDetails mockCustomUserDetails;

    @BeforeEach
    public void setUp() {
        mockUsuario = createUsuario();
        mockCustomUserDetails = createCustomUserDetails();
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(usuarioService.findByUsername(USUARIO_USERNAME)).thenReturn(mockUsuario);
        when(authFactory.toCustomUserDetails(mockUsuario)).thenReturn(mockCustomUserDetails);

        UserDetails userDetails = service.loadUserByUsername(USUARIO_USERNAME);

        assertNotNull(userDetails);
        assertEquals(mockCustomUserDetails.getUsername(), userDetails.getUsername());
        verify(usuarioService).findByUsername(USUARIO_USERNAME);
        verify(authFactory).toCustomUserDetails(mockUsuario);
    }

    @Test
    public void testLoadUserByUsernameUsuarioNotFound() {
        when(usuarioService.findByUsername(USUARIO_USERNAME)).thenThrow(new UsernameNotFoundException("Usuário não encontrado."));

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(USUARIO_USERNAME));
        verify(usuarioService).findByUsername(USUARIO_USERNAME);
    }
}
