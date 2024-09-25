CREATE TABLE token (
    id          BIGSERIAL             NOT NULL,
    token       TEXT                  NOT NULL,
    tipo        VARCHAR(100)          NOT NULL,
    revogado    BOOLEAN DEFAULT FALSE NOT NULL,
    usuario_id  BIGINT                NOT NULL
);

ALTER TABLE token
    ADD CONSTRAINT token_pk
        PRIMARY KEY (id);

ALTER TABLE token
    ADD CONSTRAINT token_usuario_fk
        FOREIGN KEY (usuario_id) REFERENCES usuario (id);

CREATE INDEX token_usuario_id_idx ON token(usuario_id);

CREATE INDEX token_idx ON token(token);
