CREATE TABLE conta (
    id                     BIGSERIAL      NOT NULL,
    valor                  DECIMAL(15, 2) NOT NULL,
    descricao              VARCHAR(400),
    situacao               VARCHAR(40)    NOT NULL,
    data_vencimento        DATE           NOT NULL,
    data_pagamento         DATE
);

ALTER TABLE conta
    ADD CONSTRAINT conta_pk
        PRIMARY KEY (id);

CREATE INDEX conta_situacao_idx ON conta(situacao);

CREATE INDEX conta_data_vencimento_idx ON conta(data_vencimento);
