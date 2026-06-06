CREATE TABLE unidades (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    cidade     VARCHAR(100) NOT NULL,
    estado     CHAR(2)      NOT NULL,
    tipo       VARCHAR(20)  NOT NULL DEFAULT 'COMPLETA',
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE produtos (
    id         BIGSERIAL     PRIMARY KEY,
    nome       VARCHAR(150)  NOT NULL,
    categoria  VARCHAR(80)   NOT NULL,
    preco_base NUMERIC(10,2) NOT NULL,
    temporada  VARCHAR(20)   NOT NULL DEFAULT 'YEAR_ROUND',
    disponivel BOOLEAN       NOT NULL DEFAULT TRUE
);

CREATE TABLE cardapio_unidade (
    id          BIGSERIAL     PRIMARY KEY,
    unidade_id  BIGINT        NOT NULL REFERENCES unidades(id),
    produto_id  BIGINT        NOT NULL REFERENCES produtos(id),
    preco_local NUMERIC(10,2) NOT NULL,
    disponivel  BOOLEAN       NOT NULL DEFAULT TRUE,
    UNIQUE(unidade_id, produto_id)
);