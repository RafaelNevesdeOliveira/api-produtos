# Fundamentos de API Java + PostgreSQL

API REST didática de produtos e categorias, construída com Java 25, Spring Boot, Spring Data JPA e PostgreSQL. Este projeto apresenta dois CRUDs independentes; relacionamentos e estoque entram no Projeto 06 / Aula 23.

## Requisitos

- JDK 25.0.2
- Maven 3.9+
- PostgreSQL em execução
- pgAdmin ou DBeaver para executar e visualizar o SQL

Confira a instalação:

```bash
java -version
mvn -version
psql --version
```

## Banco de dados

Crie o database uma única vez, usando uma conexão administrativa do PostgreSQL:

```sql
CREATE DATABASE api_fundamentos;
```

Conecte o Query Tool ou DBeaver ao database `api_fundamentos` antes de executar os scripts.

## Scripts SQL

Execute nesta ordem:

1. `sql/07_carga_passo_a_passo_relacionamentos.sql` cria `categorias`, `produtos`, a coluna `categoria_id`, a chave estrangeira e o índice.
2. Escolha uma carga:
   - `sql/08_carga_dados_relacionamentos.sql`: carga básica.
   - `sql/09_carga_base_completa.sql`: carga completa, com 8 categorias e 29 produtos.

No DBeaver/pgAdmin, abra o arquivo, execute todos os comandos e use `COMMIT;` se o auto-commit estiver desativado. Atualize a pasta `Tables` com `F5` após a execução.

Pelo terminal:

```bash
psql -U SEU_USUARIO -h localhost -d api_fundamentos \
  -f sql/07_carga_passo_a_passo_relacionamentos.sql

psql -U SEU_USUARIO -h localhost -d api_fundamentos \
  -f sql/09_carga_base_completa.sql
```

## Onde adicionar a URL de conexão

O ponto de configuração do projeto é o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/api_fundamentos}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:}
```

Você pode editar essas três propriedades para um laboratório local. Para não salvar senha no projeto, a opção recomendada é manter o arquivo como está e configurar as variáveis de ambiente abaixo.

URL JDBC local:

```text
jdbc:postgresql://localhost:5432/api_fundamentos
```

URL JDBC do Supabase: no Dashboard do projeto, clique em **Connect**, escolha **Session pooler** e copie host, porta e usuário. Para esta API Spring Boot, que é uma aplicação persistente, use Session pooler (`5432`) ou conexão direta quando sua rede suportar IPv6. Conexões Transaction pooler (`6543`) não são indicadas para esse caso porque não suportam prepared statements. Consulte a documentação oficial do [Supabase sobre conexões](https://supabase.com/docs/guides/database/connecting-to-postgres).

Converta os dados copiados para este formato:

```text
jdbc:postgresql://HOST:PORT/DATABASE?sslmode=require
```

Exemplo de variáveis para uma conexão remota:

macOS/Linux:

```bash
export DB_URL='jdbc:postgresql://HOST:PORT/api_fundamentos?sslmode=require'
export DB_USER='SEU_USUARIO'
export DB_PASSWORD='SUA_SENHA'
```

Windows PowerShell:

```powershell
$env:DB_URL = 'jdbc:postgresql://HOST:PORT/api_fundamentos?sslmode=require'
$env:DB_USER = 'SEU_USUARIO'
$env:DB_PASSWORD = 'SUA_SENHA'
```

Teste os dados antes de iniciar a API:

```sql
SELECT current_database(), current_user;
```

## Executar

Na raiz do projeto:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`.

## Testar

Swagger: <http://localhost:8080/swagger-ui.html>

Endpoints principais:

```text
GET    /api/saude
GET    /api/produtos
GET    /api/produtos/{id}
POST   /api/produtos
PUT    /api/produtos/{id}
DELETE /api/produtos/{id}
```

Exemplo de criação de produto:

```json
{
  "nome": "Teclado mecanico",
  "preco": 299.90,
  "ativo": true
}
```

Para usar o Postman, importe `postman/Fundamentos_API_Java.postman_collection.json`. O arquivo `requests.http` traz as mesmas chamadas para IntelliJ e VS Code com extensão REST Client.

## Estrutura

```text
controller (Controller + DTO)
        |
service (regras de negocio)
        |
repository (Spring Data JPA)
        |
entity (Produto)
        |
PostgreSQL
```

Os pacotes são separados por camada: `controller`, `controller.dto`, `service`, `repository`, `entity`, `exception` e `config`. O fluxo da requisição é `controller → service → repository → PostgreSQL`; as entidades representam os dados persistidos.
