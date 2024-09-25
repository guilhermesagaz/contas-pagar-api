package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.auth.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.br.contasapagar.utils.PerfilTestUtils.PERFIL_TIPO;
import static com.br.contasapagar.utils.PermissaoTestUtils.PERMISSAO_NOME;
import static com.br.contasapagar.utils.UsuarioTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class AuthFactoryTest {

    @InjectMocks
    private AuthFactory factory;

    @Test
    public void testToCustomUserDetails() {
        CustomUserDetails userDetails = factory.toCustomUserDetails(createUsuario());

        assertEquals(USUARIO_ID, userDetails.getId());
        assertEquals(USUARIO_NOME, userDetails.getNome());
        assertEquals(USUARIO_USERNAME, userDetails.getUsername());
        assertEquals(USUARIO_PASSWORD, userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertEquals(List.of(new SimpleGrantedAuthority(PERFIL_TIPO.name()),
                new SimpleGrantedAuthority(PERMISSAO_NOME)), userDetails.getAuthorities());
    }
}
