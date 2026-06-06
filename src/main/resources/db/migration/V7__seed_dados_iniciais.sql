INSERT INTO usuarios (nome, email, senha_hash, perfil, consentimento_lgpd)
VALUES
  ('Admin',         'admin@raizes.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN',     true),
  ('Maria Cliente', 'cliente@teste.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CLIENTE',   true),
  ('João Cozinha',  'cozinha@raizes.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'COZINHA',   false),
  ('Ana Gerente',   'gerente@raizes.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'GERENTE',   false),
  ('Pedro Balcão',  'balcao@raizes.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ATENDENTE', false);

INSERT INTO unidades (nome, cidade, estado, tipo)
VALUES
  ('Raízes do Nordeste — Recife Centro', 'Recife',    'PE', 'COMPLETA'),
  ('Raízes do Nordeste — Fortaleza',     'Fortaleza', 'CE', 'COMPLETA'),
  ('Raízes do Nordeste — Salvador',      'Salvador',  'BA', 'REDUZIDA');

INSERT INTO produtos (nome, categoria, preco_base, temporada)
VALUES
  ('Cuscuz Recheado',     'Prato Principal', 18.90, 'YEAR_ROUND'),
  ('Tapioca Tradicional', 'Lanche',          12.50, 'YEAR_ROUND'),
  ('Bolo de Macaxeira',   'Sobremesa',        9.90, 'YEAR_ROUND'),
  ('Suco de Cajá',        'Bebida',           8.00, 'YEAR_ROUND'),
  ('Canjica Junina',      'Sobremesa',       11.00, 'JUNINO');

INSERT INTO cardapio_unidade (unidade_id, produto_id, preco_local, disponivel)
VALUES
  (1,1,18.90,true),(1,2,12.50,true),(1,3,9.90,true),(1,4,8.00,true),(1,5,11.00,false),
  (2,1,19.50,true),(2,2,13.00,true),(2,4,8.50,true);

INSERT INTO estoque (unidade_id, produto_id, quantidade, quantidade_minima)
VALUES
  (1,1,50,10),(1,2,80,15),(1,3,30,5),(1,4,100,20),(1,5,0,5),
  (2,1,40,10),(2,2,60,15),(2,4,80,20);

INSERT INTO fidelidade (cliente_id, saldo_pontos, total_acumulado)
VALUES (2, 150, 320);