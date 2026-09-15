-- Execute depois de 07_carga_passo_a_passo_relacionamentos.sql,
-- conectado ao database api_fundamentos.

-- Garantir a coluna usada pelo relacionamento, inclusive em uma tabela
-- produtos que tenha sido criada anteriormente pelo JPA.
ALTER TABLE public.produtos
    ADD COLUMN IF NOT EXISTS categoria_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_produtos_categorias'
          AND conrelid = 'public.produtos'::regclass
    ) THEN
        ALTER TABLE public.produtos
            ADD CONSTRAINT fk_produtos_categorias
            FOREIGN KEY (categoria_id)
            REFERENCES public.categorias (id)
            ON UPDATE CASCADE
            ON DELETE SET NULL;
    END IF;
END;
$$;

-- 1. Inserir categorias.
INSERT INTO public.categorias (nome, descricao)
VALUES ('Perifericos', 'Acessorios para computadores')
ON CONFLICT (nome) DO UPDATE
SET descricao = EXCLUDED.descricao
RETURNING *;

INSERT INTO public.categorias (nome, descricao)
VALUES ('Monitores', 'Telas e equipamentos de exibicao')
ON CONFLICT (nome) DO UPDATE
SET descricao = EXCLUDED.descricao
RETURNING *;

INSERT INTO public.categorias (nome, descricao)
VALUES ('Armazenamento', 'Discos e unidades de armazenamento')
ON CONFLICT (nome) DO UPDATE
SET descricao = EXCLUDED.descricao
RETURNING *;

-- 2. Inserir produtos e ligar cada um a uma categoria.
INSERT INTO public.produtos (nome, preco, ativo, categoria_id)
SELECT 'Teclado mecanico', 299.90, TRUE, id
FROM public.categorias
WHERE nome = 'Perifericos'
  AND NOT EXISTS (
      SELECT 1 FROM public.produtos WHERE nome = 'Teclado mecanico'
  )
RETURNING *;

INSERT INTO public.produtos (nome, preco, ativo, categoria_id)
SELECT 'Mouse sem fio', 149.90, TRUE, id
FROM public.categorias
WHERE nome = 'Perifericos'
  AND NOT EXISTS (
      SELECT 1 FROM public.produtos WHERE nome = 'Mouse sem fio'
  )
RETURNING *;

INSERT INTO public.produtos (nome, preco, ativo, categoria_id)
SELECT 'Monitor 27 polegadas', 1899.90, TRUE, id
FROM public.categorias
WHERE nome = 'Monitores'
  AND NOT EXISTS (
      SELECT 1 FROM public.produtos WHERE nome = 'Monitor 27 polegadas'
  )
RETURNING *;

INSERT INTO public.produtos (nome, preco, ativo, categoria_id)
SELECT 'SSD 1TB', 499.90, TRUE, id
FROM public.categorias
WHERE nome = 'Armazenamento'
  AND NOT EXISTS (
      SELECT 1 FROM public.produtos WHERE nome = 'SSD 1TB'
  )
RETURNING *;

-- 3. Mostrar o relacionamento 1:N preenchido.
SELECT
    c.id AS categoria_id,
    c.nome AS categoria,
    p.id AS produto_id,
    p.nome AS produto,
    p.preco,
    p.ativo
FROM public.categorias c
LEFT JOIN public.produtos p
    ON p.categoria_id = c.id
ORDER BY c.nome, p.nome;

-- 4. Contar quantos produtos existem em cada categoria.
SELECT
    c.nome AS categoria,
    COUNT(p.id) AS quantidade_produtos
FROM public.categorias c
LEFT JOIN public.produtos p
    ON p.categoria_id = c.id
GROUP BY c.id, c.nome
ORDER BY c.nome;
