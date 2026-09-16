# Supabase PostgreSQL com IPv4

Mini tutorial para conectar a API Java ao PostgreSQL do Supabase usando IPv4.

## 1. Criar o projeto

1. Acesse <https://supabase.com/dashboard>.
2. Clique em **New project**.
3. Escolha organização, nome, região e senha do database.
4. Aguarde a criação terminar.

O Supabase já cria um PostgreSQL para o projeto. O database padrão normalmente é `postgres`.

## 2. Obter os dados da conexão IPv4

1. Abra o projeto no Dashboard.
2. Clique em **Connect**.
3. Escolha **Session pooler**.
4. Copie `Host`, `Port`, `Database` e `User` exibidos pelo painel.

Use Session pooler para a API Spring Boot. Essa conexão usa IPv4 e normalmente utiliza a porta `5432`.

Não crie o host manualmente: copie os valores informados pelo Supabase. O usuário do pooler costuma possuir o formato `postgres.PROJECT_REF`.

## 3. Montar a URL JDBC

O Supabase mostra uma URI parecida com esta:

```text
postgresql://USUARIO:SENHA@HOST:5432/DATABASE
```

No Java, use o formato JDBC:

```text
jdbc:postgresql://HOST:5432/DATABASE?sslmode=require
```

Exemplo:

```text
jdbc:postgresql://aws-0-us-east-2.pooler.supabase.com:5432/postgres?sslmode=require
```

Use o `DATABASE` copiado do painel. Se ele for `postgres`, não substitua por `api_fundamentos` sem antes criar esse database no mesmo servidor.

## 4. Configurar o projeto

Na raiz do projeto, configure as variáveis antes de iniciar a API.

macOS/Linux:

```bash
export DB_URL='jdbc:postgresql://HOST:5432/DATABASE?sslmode=require'
export DB_USER='USUARIO_COPIADO_DO_SUPABASE'
export DB_PASSWORD='SUA_SENHA_DO_SUPABASE'
```

Windows PowerShell:

```powershell
$env:DB_URL = 'jdbc:postgresql://HOST:5432/DATABASE?sslmode=require'
$env:DB_USER = 'USUARIO_COPIADO_DO_SUPABASE'
$env:DB_PASSWORD = 'SUA_SENHA_DO_SUPABASE'
```

Não grave a senha em `application.properties` nem no Git.

## 5. Testar a conexão

```bash
psql -h HOST -p 5432 -U USUARIO -d DATABASE -W
```

Após informar a senha, execute:

```sql
SELECT current_database(), current_user;
```

## 6. Executar a API e a carga

```bash
mvn spring-boot:run
```

Teste a API em:

```text
http://localhost:8080/api/saude
http://localhost:8080/swagger-ui.html
```

Em seguida, conectado ao mesmo database do Supabase, execute:

1. `sql/07_carga_passo_a_passo_relacionamentos.sql`
2. `sql/09_carga_base_completa.sql`

Evite Transaction pooler (`6543`) neste projeto: ele é destinado a conexões curtas e não suporta prepared statements.

Fonte: [Supabase — Connect to your database](https://supabase.com/docs/guides/database/connecting-to-postgres).
