CREATE TABLE usuario_perfil (
    usuario_id  BIGINT NOT NULL,
    perfil_id   BIGINT NOT NULL
);

ALTER TABLE usuario_perfil
    ADD CONSTRAINT usuario_perfil_pk
        PRIMARY KEY (usuario_id, perfil_id);

ALTER TABLE usuario_perfil
    ADD CONSTRAINT usuario_perfil_perfil_fk
        FOREIGN KEY (perfil_id) REFERENCES perfil (id);

ALTER TABLE usuario_perfil
    ADD CONSTRAINT usuario_perfil_usuario_fk
        FOREIGN KEY (usuario_id) REFERENCES usuario (id);
