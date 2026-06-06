CREATE TABLE estoque (
    id                BIGSERIAL PRIMARY KEY,
    unidade_id        BIGINT    NOT NULL REFERENCES unidades(id),
    produto_id        BIGINT    NOT NULL REFERENCES produtos(id),
    quantidade        INT       NOT NULL DEFAULT 0 CHECK (quantidade >= 0),
    quantidade_minima INT       NOT NULL DEFAULT 10,
    updated_at        TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(unidade_id, produto_id)
);