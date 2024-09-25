CREATE TABLE envio_arquivo (
    id                 BIGSERIAL    NOT NULL,
    descricao          VARCHAR(200) NOT NULL,
    status             VARCHAR(40)  NOT NULL,
    arquivo            BYTEA        NOT NULL,
    mensagem           TEXT,
    data_envio         TIMESTAMP    NOT NULL,
    data_conclusao     TIMESTAMP
);

ALTER TABLE envio_arquivo
    ADD CONSTRAINT envio_arquivo_pk
        PRIMARY KEY (id);
