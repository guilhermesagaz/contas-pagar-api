package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.permissao.PermissaoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.br.contasapagar.utils.PermissaoTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class PermissaoFactoryTest {

    @InjectMocks
    private PermissaoFactory factory;

    @Test
    void testToResponse() {
        PermissaoResponse response = factory.toResponse(createPermissao());

        assertEquals(PERMISSAO_ID, response.getId());
        assertEquals(PERMISSAO_NOME, response.getNome());
        assertEquals(PERMISSAO_DESCRICAO, response.getDescricao());
    }
}
