CREATE TABLE perfil (
    id         BIGSERIAL    NOT NULL,
    tipo       VARCHAR(100) NOT NULL,
    nome       VARCHAR(100) NOT NULL,
    descricao  VARCHAR(400)
);

ALTER TABLE perfil
    ADD CONSTRAINT perfil_pk
        PRIMARY KEY (id);

ALTER TABLE perfil
    ADD CONSTRAINT nome_perfil_uk UNIQUE (nome);

CREATE INDEX perfil_nome_idx ON perfil(nome);
