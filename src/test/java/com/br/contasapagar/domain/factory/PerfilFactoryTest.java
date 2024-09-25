package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.perfil.PerfilResponse;
import com.br.contasapagar.domain.model.Perfil;
import com.br.contasapagar.domain.service.PermissaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.br.contasapagar.utils.PerfilTestUtils.*;
import static com.br.contasapagar.utils.PermissaoTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PerfilFactoryTest {

    @InjectMocks
    private PerfilFactory factory;

    @Mock
    private PermissaoService permissaoService;

    @Mock
    private PermissaoFactory permissaoFactory;

    @Test
    public void testToEntity() {
        when(permissaoService.findById(1L)).thenReturn(createPermissao());

        Perfil perfil = factory.toEntity(createPerfilRequest());

        assertEquals(PERFIL_TIPO, perfil.getTipo());
        assertEquals(PERFIL_NOME, perfil.getNome());
        assertEquals(PERFIL_DESCRICAO, perfil.getDescricao());
        assertEquals(PERMISSAO_ID, perfil.getPermissoes().get(0).getId());
        assertEquals(PERMISSAO_NOME, perfil.getPermissoes().get(0).getNome());
    }

    @Test
    public void testToResponse() {
        when(permissaoFactory.toResponse(any())).thenReturn(createPermissaoResponse());

        PerfilResponse response = factory.toResponse(createPerfil());

        assertEquals(PERFIL_ID, response.getId());
        assertEquals(PERFIL_TIPO, response.getTipo());
        assertEquals(PERFIL_NOME, response.getNome());
        assertEquals(PERFIL_DESCRICAO, response.getDescricao());
        assertEquals(1, response.getPermissoes().size());
        assertEquals(PERMISSAO_ID, response.getPermissoes().get(0).getId());
        assertEquals(PERMISSAO_NOME, response.getPermissoes().get(0).getNome());
    }
}
