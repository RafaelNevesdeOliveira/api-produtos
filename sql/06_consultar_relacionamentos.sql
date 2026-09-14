-- Execute conectado ao banco api_fundamentos.

-- 1:N: produtos e suas categorias.
SELECT
    p.id,
    p.nome AS produto,
    p.preco,
    c.nome AS categoria
FROM produtos p
LEFT JOIN categorias c ON c.id = p.categoria_id
ORDER BY c.nome, p.nome;

-- Quantidade de produtos por categoria, incluindo categorias vazias.
SELECT
    c.id,
    c.nome AS categoria,
    COUNT(p.id) AS quantidade_produtos
FROM categorias c
LEFT JOIN produtos p ON p.categoria_id = c.id
GROUP BY c.id, c.nome
ORDER BY c.nome;

-- 1:1: posição de estoque de cada produto.
SELECT
    p.id,
    p.nome,
    COALESCE(e.quantidade, 0) AS quantidade,
    COALESCE(e.estoque_minimo, 0) AS estoque_minimo,
    CASE
        WHEN e.id IS NULL THEN 'SEM CADASTRO'
        WHEN e.quantidade <= e.estoque_minimo THEN 'REPOR'
        ELSE 'OK'
    END AS situacao_estoque
FROM produtos p
LEFT JOIN estoques e ON e.produto_id = p.id
ORDER BY p.nome;

-- N:N: fornecedores disponíveis para cada produto.
SELECT
    p.nome AS produto,
    f.nome AS fornecedor,
    pf.codigo_no_fornecedor,
    pf.custo,
    pf.prazo_dias
FROM produtos_fornecedores pf
JOIN produtos p ON p.id = pf.produto_id
JOIN fornecedores f ON f.id = pf.fornecedor_id
WHERE f.ativo = TRUE
ORDER BY p.nome, pf.custo;

-- Melhor custo cadastrado para cada produto.
SELECT
    p.id,
    p.nome,
    MIN(pf.custo) AS menor_custo,
    COUNT(pf.fornecedor_id) AS quantidade_fornecedores
FROM produtos p
LEFT JOIN produtos_fornecedores pf ON pf.produto_id = p.id
GROUP BY p.id, p.nome
ORDER BY p.nome;
