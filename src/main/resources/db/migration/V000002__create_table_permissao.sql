CREATE TABLE permissao (
    id                  BIGSERIAL    NOT NULL,
    nome                VARCHAR(200) NOT NULL,
    descricao           VARCHAR(400)
);

ALTER TABLE permissao
    ADD CONSTRAINT permissao_pk
    PRIMARY KEY (id);
