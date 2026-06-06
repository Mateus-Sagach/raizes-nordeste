CREATE TABLE fidelidade (
    id              BIGSERIAL PRIMARY KEY,
    cliente_id      BIGINT    NOT NULL UNIQUE REFERENCES usuarios(id),
    saldo_pontos    INT       NOT NULL DEFAULT 0 CHECK (saldo_pontos >= 0),
    total_acumulado INT       NOT NULL DEFAULT 0,
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE transacoes_pontos (
    id            BIGSERIAL    PRIMARY KEY,
    fidelidade_id BIGINT       NOT NULL REFERENCES fidelidade(id),
    pedido_id     BIGINT       REFERENCES pedidos(id),
    tipo          VARCHAR(20)  NOT NULL,
    pontos        INT          NOT NULL,
    descricao     VARCHAR(200),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE audit_log (
    id          BIGSERIAL   PRIMARY KEY,
    usuario_id  BIGINT      REFERENCES usuarios(id),
    acao        VARCHAR(80) NOT NULL,
    entidade    VARCHAR(80),
    entidade_id BIGINT,
    dados_json  TEXT,
    ip          VARCHAR(45),
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);