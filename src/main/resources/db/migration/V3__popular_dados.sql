-- ==========================================
-- 1. INSERTS DE ESTABELECIMENTOS
-- ==========================================
INSERT INTO estabelecimento (nome, cnpj, logradouro, bairro, cidade, uf) VALUES
('Supermercado Central', '11111111000111', 'Rua Barao do Rio Branco, 500', 'Centro', 'Fortaleza', 'CE'),
('Farmacia do Trabalhador', '22222222000122', 'Rua Guilherme Rocha, 120', 'Centro', 'Fortaleza', 'CE'),
('Posto de Combustivel Jose', '33333333000133', 'Avenida Duque de Caxias, 850', 'Centro', 'Fortaleza', 'CE');
INSERT INTO estabelecimento (nome, cnpj, logradouro, bairro, cidade, uf) VALUES
('Joalheria Luxo SP', '44444444000144', 'Avenida Marques de Sao Vicente, 1200', 'Barra Funda', 'Sao Paulo', 'SP');
INSERT INTO estabelecimento (nome, cnpj, logradouro, bairro, cidade, uf) VALUES
('Eletronicos Rio Premium', '55555555000155', 'Avenida Nossa Senhora de Copacabana, 450', 'Copacabana', 'Rio de Janeiro', 'RJ');


-- ---------------------------------
-- INSERTS DE CARTÕES
-- --------------------------------
-- O cartão que sofrerá os ataques de fraude (Saldo elevado inicialmente de R$ 5000)
INSERT INTO cartao (numero_cartao, senha, saldo) VALUES
('6543210987654321', '1234', 5000.00);

-- Outros cartões aleatórios para compor o banco
INSERT INTO cartao (numero_cartao, senha, saldo) VALUES
('1111222233334444', '4321', 150.00),
('5555666677778888', '9876', 1200.50);


-- --------------------------------------------------------
-- INSERTS DE TRANSAÇÕES (Simulação de Fraude no Cartão ID 1)
-- ----------------------------------------------------------
-- Transação 1: Comportamento normal do dono (Compra rotineira de manhã em Fortaleza)
INSERT INTO transacao (cartao_id, estabelecimento_id, valor, status, observacao, data_transacao) VALUES
(1, 1, 85.50, 'APROVADA', 'Compra mensal de rotina', '2026-06-19 09:15:00');

-- Transação 2: Comportamento normal do dono (Compra rotineira à noite em Fortaleza)
INSERT INTO transacao (cartao_id, estabelecimento_id, valor, status, observacao, data_transacao) VALUES
(1, 2, 42.00, 'APROVADA', 'Medicamentos', '2026-06-19 20:30:00');

-- INÍCIO DO PADRÃO DE FRAUDE DE MADRUGADA

-- Transação 3: Compra suspeita de valor alto em São Paulo 3 horas depois (Incompatibilidade geográfica!)
INSERT INTO transacao (cartao_id, estabelecimento_id, valor, status, observacao, data_transacao) VALUES
(1, 4, 1500.00, 'APROVADA', 'Possivel clonagem - Distancia incompativel', '2026-06-19 23:45:00');

-- Transação 4: Tentativa em SP logo em seguida com valor absurdo (Fraude Limite Excedido/Saldo Insuficiente)
INSERT INTO transacao (cartao_id, estabelecimento_id, valor, status, observacao, data_transacao) VALUES
(1, 4, 4500.00, 'RECUSADA', 'Saldo insuficiente para a tentativa de fraude', '2026-06-19 23:47:00');

-- Transação 5: Novo estouro de segurança 10 minutos depois, agora no Rio de Janeiro (Ataque simultâneo em vários estados)
INSERT INTO transacao (cartao_id, estabelecimento_id, valor, status, observacao, data_transacao) VALUES
(1, 5, 2300.00, 'APROVADA', 'Possivel clonagem - Disparada em multiplos estados', '2026-06-19 23:58:00');