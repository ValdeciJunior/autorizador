-- 1. Criação da Tabela de Estabelecimentos
CREATE TABLE estabelecimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    logradouro VARCHAR(150) NOT NULL,
    bairro VARCHAR(50) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    uf CHAR(2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Criação da Tabela de Transações
CREATE TABLE transacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cartao_id BIGINT NOT NULL,
    estabelecimento_id BIGINT NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL, -- Guardará os ENUMs: APROVADA, RECUSADA, ESTORNADA, RECARGA
    observacao VARCHAR(255),
    data_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Índices para otimizar buscas históricas
    INDEX idx_transacao_cartao (cartao_id),
    INDEX idx_transacao_estabelecimento (estabelecimento_id),

    -- Restrições de Chave Estrangeira garantindo assim integridade no MySQL
    CONSTRAINT fk_transacao_cartao FOREIGN KEY (cartao_id)
        REFERENCES cartao (id) ON DELETE RESTRICT,

    CONSTRAINT fk_transacao_estabelecimento FOREIGN KEY (estabelecimento_id)
        REFERENCES estabelecimento (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;