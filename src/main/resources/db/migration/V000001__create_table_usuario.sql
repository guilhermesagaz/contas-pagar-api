CREATE TABLE usuario (
    id                     BIGSERIAL             NOT NULL,
    nome                   VARCHAR(150)          NOT NULL,
    username               VARCHAR(255)          NOT NULL,
    password               VARCHAR(255),
    expirado               BOOLEAN DEFAULT FALSE NOT NULL,
    bloqueado              BOOLEAN DEFAULT FALSE NOT NULL,
    credenciais_expiradas  BOOLEAN DEFAULT FALSE NOT NULL,
--    verificado             BOOLEAN DEFAULT FALSE NOT NULL,
    ativo                  BOOLEAN DEFAULT TRUE  NOT NULL
);

ALTER TABLE usuario
    ADD CONSTRAINT usuario_pk
        PRIMARY KEY (id);

ALTER TABLE usuario
    ADD CONSTRAINT username_uk UNIQUE (username);

CREATE INDEX usuario_username_idx ON usuario(username);
