CREATE TABLE permissao_perfil (
    perfil_id     BIGINT NOT NULL,
    permissao_id  BIGINT NOT NULL
);

ALTER TABLE permissao_perfil
    ADD CONSTRAINT permissao_perfil_pk
        PRIMARY KEY (perfil_id, permissao_id);

ALTER TABLE permissao_perfil
    ADD CONSTRAINT permissao_perfil_permissao_fk
        FOREIGN KEY (permissao_id) REFERENCES permissao (id);

ALTER TABLE permissao_perfil
    ADD CONSTRAINT permissao_perfil_perfil_fk
        FOREIGN KEY (perfil_id) REFERENCES perfil (id);
