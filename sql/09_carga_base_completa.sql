-- Carga ampliada para a aula.
-- Execute conectado ao database api_fundamentos, depois do arquivo 07.
-- O script trabalha somente com as tabelas categorias e produtos.

-- 1. Garantir a coluna e o relacionamento, inclusive quando produtos
-- foi criada anteriormente pelo JPA sem a coluna categoria_id.
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

-- 2. Criar ou atualizar as categorias da base.
INSERT INTO public.categorias (nome, descricao)
VALUES
    ('Perifericos', 'Acessorios para computadores'),
    ('Monitores', 'Telas e equipamentos de exibicao'),
    ('Armazenamento', 'Discos e unidades de armazenamento'),
    ('Notebooks', 'Computadores portateis'),
    ('Componentes', 'Pecas internas para computadores'),
    ('Redes', 'Equipamentos de conectividade'),
    ('Audio', 'Equipamentos de audio e comunicacao'),
    ('Acessorios', 'Cabos, adaptadores e itens complementares')
ON CONFLICT (nome) DO UPDATE
SET descricao = EXCLUDED.descricao;

-- 3. Cadastrar produtos em cada categoria.
WITH dados_produtos (nome, preco, ativo, categoria) AS (
    VALUES
        ('Teclado mecanico', 299.90, TRUE, 'Perifericos'),
        ('Teclado compacto sem fio', 249.90, TRUE, 'Perifericos'),
        ('Mouse sem fio', 149.90, TRUE, 'Perifericos'),
        ('Mouse gamer RGB', 199.90, TRUE, 'Perifericos'),
        ('Webcam Full HD', 279.90, TRUE, 'Perifericos'),
        ('Monitor 24 polegadas', 899.90, TRUE, 'Monitores'),
        ('Monitor 27 polegadas', 1899.90, TRUE, 'Monitores'),
        ('Monitor ultrawide 34 polegadas', 2799.90, TRUE, 'Monitores'),
        ('SSD 480GB', 289.90, TRUE, 'Armazenamento'),
        ('SSD 1TB', 499.90, TRUE, 'Armazenamento'),
        ('HD externo 2TB', 629.90, TRUE, 'Armazenamento'),
        ('Pen drive 64GB', 49.90, TRUE, 'Armazenamento'),
        ('Notebook i5 16GB RAM', 4299.90, TRUE, 'Notebooks'),
        ('Notebook i7 16GB RAM', 5999.90, TRUE, 'Notebooks'),
        ('Notebook Ryzen 7 16GB RAM', 5499.90, TRUE, 'Notebooks'),
        ('Memoria RAM 8GB DDR4', 189.90, TRUE, 'Componentes'),
        ('Memoria RAM 16GB DDR4', 329.90, TRUE, 'Componentes'),
        ('Fonte 650W 80 Plus', 419.90, TRUE, 'Componentes'),
        ('Placa de video 8GB', 2499.90, TRUE, 'Componentes'),
        ('Roteador Wi-Fi 6', 449.90, TRUE, 'Redes'),
        ('Switch 8 portas gigabit', 279.90, TRUE, 'Redes'),
        ('Adaptador USB para rede', 79.90, TRUE, 'Redes'),
        ('Headset USB', 229.90, TRUE, 'Audio'),
        ('Caixa de som Bluetooth', 189.90, TRUE, 'Audio'),
        ('Microfone USB', 359.90, TRUE, 'Audio'),
        ('Cabo HDMI 2 metros', 39.90, TRUE, 'Acessorios'),
        ('Cabo USB-C 1 metro', 29.90, TRUE, 'Acessorios'),
        ('Hub USB 4 portas', 119.90, TRUE, 'Acessorios'),
        ('Suporte para notebook', 99.90, TRUE, 'Acessorios')
)
INSERT INTO public.produtos (nome, preco, ativo, categoria_id)
SELECT
    d.nome,
    d.preco,
    d.ativo,
    c.id
FROM dados_produtos d
INNER JOIN public.categorias c
    ON c.nome = d.categoria
WHERE NOT EXISTS (
    SELECT 1
    FROM public.produtos p
    WHERE p.nome = d.nome
);

-- 4. Mostrar todos os produtos com suas categorias.
SELECT
    c.nome AS categoria,
    p.id AS produto_id,
    p.nome AS produto,
    p.preco,
    p.ativo
FROM public.categorias c
LEFT JOIN public.produtos p
    ON p.categoria_id = c.id
ORDER BY c.nome, p.nome;

-- 5. Resumo da base por categoria.
SELECT
    c.nome AS categoria,
    COUNT(p.id) AS quantidade_produtos,
    COALESCE(SUM(p.preco), 0) AS valor_total
FROM public.categorias c
LEFT JOIN public.produtos p
    ON p.categoria_id = c.id
GROUP BY c.id, c.nome
ORDER BY c.nome;

-- 6. Total de registros carregados.
SELECT
    (SELECT COUNT(*) FROM public.categorias) AS total_categorias,
    (SELECT COUNT(*) FROM public.produtos) AS total_produtos;
