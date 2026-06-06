CREATE TABLE pedidos (
    id           BIGSERIAL     PRIMARY KEY,
    cliente_id   BIGINT        NOT NULL REFERENCES usuarios(id),
    unidade_id   BIGINT        NOT NULL REFERENCES unidades(id),
    canal_pedido VARCHAR(20)   NOT NULL,
    status       VARCHAR(30)   NOT NULL DEFAULT 'AGUARDANDO_PAGAMENTO',
    total        NUMERIC(10,2) NOT NULL,
    created_at   TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE TABLE itens_pedido (
    id             BIGSERIAL     PRIMARY KEY,
    pedido_id      BIGINT        NOT NULL REFERENCES pedidos(id),
    produto_id     BIGINT        NOT NULL REFERENCES produtos(id),
    quantidade     INT           NOT NULL CHECK (quantidade > 0),
    preco_unitario NUMERIC(10,2) NOT NULL
);

CREATE INDEX idx_pedidos_canal   ON pedidos(canal_pedido);
CREATE INDEX idx_pedidos_status  ON pedidos(status);
CREATE INDEX idx_pedidos_cliente ON pedidos(cliente_id);
CREATE INDEX idx_pedidos_unidade ON pedidos(unidade_id);