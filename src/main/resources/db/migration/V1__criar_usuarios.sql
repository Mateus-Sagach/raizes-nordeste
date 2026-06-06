CREATE TABLE usuarios (
    id                 BIGSERIAL    PRIMARY KEY,
    nome               VARCHAR(150) NOT NULL,
    email              VARCHAR(150) NOT NULL UNIQUE,
    senha_hash         VARCHAR(255) NOT NULL,
    perfil             VARCHAR(30)  NOT NULL,
    ativo              BOOLEAN      NOT NULL DEFAULT TRUE,
    consentimento_lgpd BOOLEAN      NOT NULL DEFAULT FALSE,
    data_consentimento TIMESTAMP,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_usuarios_email ON usuarios(email);