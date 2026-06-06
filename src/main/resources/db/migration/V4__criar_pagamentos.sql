CREATE TABLE pagamentos (
    id              BIGSERIAL     PRIMARY KEY,
    pedido_id       BIGINT        NOT NULL UNIQUE REFERENCES pedidos(id),
    forma_pagamento VARCHAR(20)   NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'PENDENTE',
    valor           NUMERIC(10,2) NOT NULL,
    gateway_ref     VARCHAR(100),
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW()
);