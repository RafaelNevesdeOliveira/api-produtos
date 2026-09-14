-- Execute depois de 04_criar_tabelas_relacionadas.sql.

INSERT INTO categorias (nome, descricao)
VALUES
    ('Perifericos', 'Acessorios utilizados com computadores'),
    ('Monitores', 'Telas e equipamentos de exibicao'),
    ('Armazenamento', 'Discos e unidades de armazenamento')
ON CONFLICT (nome) DO UPDATE
SET descricao = EXCLUDED.descricao;

INSERT INTO fornecedores (nome, documento, email, ativo)
VALUES
    ('Fornecedor Alpha', 'FORN-001', 'alpha@example.com', TRUE),
    ('Fornecedor Beta', 'FORN-002', 'beta@example.com', TRUE)
ON CONFLICT (documento) DO UPDATE
SET nome = EXCLUDED.nome,
    email = EXCLUDED.email,
    ativo = EXCLUDED.ativo;

INSERT INTO produtos (nome, preco, ativo, categoria_id)
SELECT 'Teclado mecanico', 299.90, TRUE, c.id
FROM categorias c
WHERE c.nome = 'Perifericos'
  AND NOT EXISTS (
      SELECT 1 FROM produtos p WHERE p.nome = 'Teclado mecanico'
  );

INSERT INTO produtos (nome, preco, ativo, categoria_id)
SELECT 'Monitor 27 polegadas', 1899.90, TRUE, c.id
FROM categorias c
WHERE c.nome = 'Monitores'
  AND NOT EXISTS (
      SELECT 1 FROM produtos p WHERE p.nome = 'Monitor 27 polegadas'
  );

-- Relaciona produtos já existentes às categorias.
UPDATE produtos
SET categoria_id = (
    SELECT id FROM categorias WHERE nome = 'Perifericos'
)
WHERE nome ILIKE '%teclado%'
   OR nome ILIKE '%mouse%';

-- Cria ou atualiza o estoque dos produtos de exemplo.
INSERT INTO estoques (produto_id, quantidade, estoque_minimo)
SELECT p.id, 25, 5
FROM produtos p
WHERE p.nome = 'Teclado mecanico'
ON CONFLICT (produto_id) DO UPDATE
SET quantidade = EXCLUDED.quantidade,
    estoque_minimo = EXCLUDED.estoque_minimo,
    atualizado_em = CURRENT_TIMESTAMP;

INSERT INTO estoques (produto_id, quantidade, estoque_minimo)
SELECT p.id, 8, 3
FROM produtos p
WHERE p.nome = 'Monitor 27 polegadas'
ON CONFLICT (produto_id) DO UPDATE
SET quantidade = EXCLUDED.quantidade,
    estoque_minimo = EXCLUDED.estoque_minimo,
    atualizado_em = CURRENT_TIMESTAMP;

-- Vincula o mesmo produto a dois fornecedores.
INSERT INTO produtos_fornecedores (
    produto_id,
    fornecedor_id,
    codigo_no_fornecedor,
    custo,
    prazo_dias
)
SELECT p.id, f.id, 'ALPHA-TEC-01', 210.00, 4
FROM produtos p
CROSS JOIN fornecedores f
WHERE p.nome = 'Teclado mecanico'
  AND f.documento = 'FORN-001'
ON CONFLICT (produto_id, fornecedor_id) DO UPDATE
SET codigo_no_fornecedor = EXCLUDED.codigo_no_fornecedor,
    custo = EXCLUDED.custo,
    prazo_dias = EXCLUDED.prazo_dias;

INSERT INTO produtos_fornecedores (
    produto_id,
    fornecedor_id,
    codigo_no_fornecedor,
    custo,
    prazo_dias
)
SELECT p.id, f.id, 'BETA-TEC-99', 205.50, 7
FROM produtos p
CROSS JOIN fornecedores f
WHERE p.nome = 'Teclado mecanico'
  AND f.documento = 'FORN-002'
ON CONFLICT (produto_id, fornecedor_id) DO UPDATE
SET codigo_no_fornecedor = EXCLUDED.codigo_no_fornecedor,
    custo = EXCLUDED.custo,
    prazo_dias = EXCLUDED.prazo_dias;
