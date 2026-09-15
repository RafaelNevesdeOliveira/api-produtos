# Execução completa da API Java com PostgreSQL e pgAdmin sem Docker

Este guia prepara um ambiente local e executa o projeto `fundamentos-api` em Windows, Linux ou macOS. A aplicação expõe uma API REST de produtos em `http://localhost:8080`, usa Spring Boot, Spring Data JPA e grava os dados em um servidor PostgreSQL instalado na própria máquina. O pgAdmin administra o banco, mas não substitui o servidor PostgreSQL.

## Roteiro principal: do zero ate a carga

Siga esta ordem. Ela vale para pgAdmin, DBeaver ou terminal.

### 1. Criar a conexao com o PostgreSQL

No pgAdmin, registre um servidor com os dados abaixo. No DBeaver, crie uma conexão PostgreSQL com os mesmos dados.

| Campo | Ambiente local |
| --- | --- |
| Host | `localhost` |
| Porta | `5432` |
| Database inicial | `postgres` |
| Usuario | o usuario criado na instalacao, normalmente `postgres` |
| Senha | a senha definida durante a instalacao |

Depois de conectar no database `postgres`, crie o database do projeto. Execute este comando sozinho, com auto-commit ligado:

```sql
CREATE DATABASE api_fundamentos;
```

Atualize a árvore do cliente, conecte no database `api_fundamentos` e confirme:

```sql
SELECT current_database(), current_user;
```

### 2. Obter ou montar a URL JDBC

Para PostgreSQL local, a URL é formada por host, porta e database:

```text
jdbc:postgresql://localhost:5432/api_fundamentos
```

Para Supabase, crie/abra o projeto, clique em **Connect** no Dashboard e escolha **Session pooler** para esta aplicação Spring Boot. Copie host, porta, usuario e database apresentados pelo painel e monte a URL JDBC:

```text
jdbc:postgresql://HOST:PORT/DATABASE?sslmode=require
```

O Supabase informa que a string é obtida pelo botão **Connect**; aplicações persistentes devem usar conexão direta ou Session pooler, enquanto Transaction pooler é destinado a conexões curtas e não suporta prepared statements. Veja a [documentacao oficial do Supabase](https://supabase.com/docs/guides/database/connecting-to-postgres).

### 3. Adicionar a conexão no projeto

O arquivo do projeto é `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/api_fundamentos}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:}
```

Para uma conexão local com outro usuário, ou para Supabase, não altere senha no Git. Informe os valores por variáveis de ambiente.

macOS/Linux:

```bash
export DB_URL='jdbc:postgresql://HOST:PORT/DATABASE?sslmode=require'
export DB_USER='SEU_USUARIO'
export DB_PASSWORD='SUA_SENHA'
```

Windows PowerShell:

```powershell
$env:DB_URL = 'jdbc:postgresql://HOST:PORT/DATABASE?sslmode=require'
$env:DB_USER = 'SEU_USUARIO'
$env:DB_PASSWORD = 'SUA_SENHA'
```

### 4. Conectar e rodar a API

Na pasta que contém `pom.xml`:

```bash
mvn clean test
mvn spring-boot:run
```

Quando aparecer que a aplicação iniciou na porta `8080`, teste:

```text
http://localhost:8080/api/saude
http://localhost:8080/swagger-ui.html
```

### 5. Executar a carga por ultimo

Com a API parada ou em execução, abra o cliente SQL conectado ao database `api_fundamentos` e execute:

1. `sql/07_carga_passo_a_passo_relacionamentos.sql` para criar `categorias`, `produtos` e a chave estrangeira.
2. `sql/09_carga_base_completa.sql` para inserir 8 categorias e 29 produtos.

Se o auto-commit estiver desligado, finalize com:

```sql
COMMIT;
```

Atualize a pasta `Tables` com `F5` e confira a carga:

```sql
SELECT COUNT(*) AS total_produtos
FROM public.produtos;
```

## Arquitetura local

```text
Cliente HTTP
    |
    | http://localhost:8080/api/produtos
    v
Spring Boot + Tomcat embutido
    |
    | JDBC jdbc:postgresql://localhost:5432/api_fundamentos
    v
PostgreSQL local <---- pgAdmin 4
```

## Requisitos

- JDK 25.0.2, versão homologada para o projeto.
- Maven 3.6.3 ou superior.
- PostgreSQL instalado e com o serviço em execução.
- pgAdmin 4 Desktop para acompanhar o banco graficamente.
- Um cliente HTTP: `curl`, arquivo `requests.http`, Postman ou Insomnia.
- Internet apenas na primeira compilação, para o Maven baixar dependências.

Confirme Java e Maven:

```bash
java -version
mvn -version
```

O comando `mvn -version` deve mostrar o mesmo JDK esperado. Se ele apontar para outro Java, ajuste `JAVA_HOME` e reinicie o terminal.
A saída de `java -version` e a JVM exibida por `mvn -version` devem indicar exatamente `25.0.2`.

## Instalação no Windows

1. Instale o JDK 25.0.2 homologado e marque a opção para configurar `JAVA_HOME`, quando disponível. Uma distribuição possível é o Eclipse Temurin 25 em <https://adoptium.net/temurin/releases/?version=25>.
2. Instale o Maven e inclua a pasta `bin` no `Path`.
3. Baixe o instalador do PostgreSQL em <https://www.postgresql.org/download/windows/>. O instalador gráfico normalmente oferece PostgreSQL e pgAdmin.
4. Durante a instalação, mantenha a porta `5432`, defina uma senha para o superusuário `postgres` e anote essa senha.
5. Abra `Serviços` e confirme que o serviço cujo nome começa com `postgresql` está em execução.
6. Abra o pgAdmin 4 pelo menu Iniciar.

Exemplo de configuração manual das variáveis, se necessário:

```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot", "User")
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\apache-maven-3.9.11\bin", "User")
```

Feche e reabra o PowerShell depois de alterar variáveis permanentes.

## Instalação no macOS

Opção gráfica simples:

1. Instale o JDK 25.0.2 homologado. O Eclipse Temurin disponibiliza instalador para macOS em <https://adoptium.net/temurin/releases/?version=25>.
2. Instale o Maven.
3. Use o instalador para macOS indicado em <https://www.postgresql.org/download/macosx/>. O instalador EDB inclui servidor PostgreSQL e pgAdmin.
4. Mantenha a porta `5432`, defina a senha do usuário `postgres` e inicie o serviço ao fim da instalação.

Opção Homebrew:

```bash
brew install maven postgresql@18 --formula
brew services start postgresql@18
```

O pgAdmin pode ser instalado separadamente pelo pacote oficial em <https://www.pgadmin.org/download/pgadmin-4-macos/>.

Após instalar o JDK homologado, use `/usr/libexec/java_home -V` para confirmar a instalação e configure `JAVA_HOME` para o JDK 25.0.2 quando houver outras versões na máquina.

## Instalação no Linux

### Ubuntu e Debian

Instale primeiro o JDK 25.0.2 homologado pelo pacote ou arquivo da distribuição adotada pela instituição. Em seguida, instale Maven e PostgreSQL:

```bash
sudo apt update
sudo apt install maven postgresql postgresql-contrib
sudo systemctl enable --now postgresql
```

O pgAdmin mantém instruções atualizadas para pacotes DEB em <https://www.pgadmin.org/download/pgadmin-4-apt/>. Para uma instalação somente desktop, selecione o pacote `pgadmin4-desktop` indicado nessa página.

### Fedora

Instale primeiro o JDK 25.0.2 homologado pelo pacote ou arquivo da distribuição adotada pela instituição. Depois execute:

```bash
sudo dnf install maven postgresql-server postgresql-contrib
sudo postgresql-setup --initdb
sudo systemctl enable --now postgresql
```

O pgAdmin mantém instruções para RPM em <https://www.pgadmin.org/download/pgadmin-4-rpm/>. Instale a variante desktop quando quiser a interface local.

Em distribuições diferentes, use os pacotes oficiais indicados em <https://www.postgresql.org/download/linux/>.

## Cadastro do servidor no pgAdmin

1. Abra o pgAdmin 4.
2. Clique com o botão direito em `Servers` e escolha `Register` e `Server`.
3. Em `General`, use o nome `PostgreSQL Local`.
4. Em `Connection`, preencha:

| Campo | Valor |
| --- | --- |
| Host name address | `localhost` |
| Port | `5432` |
| Maintenance database | `postgres` |
| Username | `postgres` |
| Password | a senha definida na instalação |

5. Salve. Se o cadastro falhar, confirme o serviço, a porta e a senha antes de alterar arquivos do PostgreSQL.

## Scripts SQL de produtos e categorias

### Execução recomendada — produtos e categorias

Os scripts abaixo assumem que o database `api_fundamentos` já existe e que você está conectado nele. Eles não criam usuário, database, estoque ou fornecedores.

O arquivo `sql/07_carga_passo_a_passo_relacionamentos.sql` cria as tabelas `categorias` e `produtos`, a chave estrangeira e as consultas de estrutura. O arquivo `sql/08_carga_dados_relacionamentos.sql` insere categorias e produtos e demonstra o relacionamento `1:N`.

Para uma base maior, use `sql/09_carga_base_completa.sql` no lugar do arquivo `08`. Ele adiciona oito categorias e 29 produtos de exemplo.

Execute pelo terminal, a partir da pasta do projeto:

```bash
psql -U SEU_USUARIO -h localhost -d api_fundamentos -f sql/07_carga_passo_a_passo_relacionamentos.sql
# Carga basica:
psql -U SEU_USUARIO -h localhost -d api_fundamentos -f sql/08_carga_dados_relacionamentos.sql
# Ou carga completa:
psql -U SEU_USUARIO -h localhost -d api_fundamentos -f sql/09_carga_base_completa.sql
```

No Linux, quando a instalação usa autenticação administrativa pelo sistema operacional:

```bash
sudo -u postgres psql -d api_fundamentos -f sql/07_carga_passo_a_passo_relacionamentos.sql
# Carga basica:
psql -U SEU_USUARIO -h localhost -d api_fundamentos -f sql/08_carga_dados_relacionamentos.sql
# Ou carga completa:
psql -U SEU_USUARIO -h localhost -d api_fundamentos -f sql/09_carga_base_completa.sql
```

Os três arquivos podem ser abertos no pgAdmin e executados uma query por vez. Eles são idempotentes.

Para Supabase, copie host, porta e usuário no painel **Connect**. Substitua os valores abaixo pelos dados copiados:

```bash
psql -h HOST -p PORTA \
  -U USUARIO -d DATABASE -W \
  -f sql/07_carga_passo_a_passo_relacionamentos.sql

psql -h HOST -p PORTA \
  -U USUARIO -d DATABASE -W \
  -f sql/09_carga_base_completa.sql
```

## Conteúdo dos arquivos SQL

A pasta `sql` contém três arquivos, com responsabilidades separadas:

- `07_carga_passo_a_passo_relacionamentos.sql`: tabelas `categorias` e `produtos`, relacionamento e consultas de estrutura.
- `08_carga_dados_relacionamentos.sql`: carga de categorias e produtos e consultas com `JOIN`.
- `09_carga_base_completa.sql`: carga ampliada com oito categorias e 29 produtos.

Modelo criado:

```text
categorias 1 -------- N produtos
```

## Configuração da aplicação

Os valores didáticos ficam em `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/api_fundamentos}
spring.datasource.username=${DB_USER:api_user}
spring.datasource.password=${DB_PASSWORD:api123}
```

O texto depois de `:` é o padrão local. Variáveis de ambiente substituem esses padrões sem modificar o arquivo. Para informar URL, usuário e senha:

macOS ou Linux:

```bash
export DB_URL='jdbc:postgresql://HOST:PORT/DATABASE?sslmode=require'
export DB_USER='SEU_USUARIO'
export DB_PASSWORD='SUA_SENHA'
mvn spring-boot:run
```

PowerShell:

```powershell
$env:DB_URL='jdbc:postgresql://HOST:PORT/DATABASE?sslmode=require'
$env:DB_USER='SEU_USUARIO'
$env:DB_PASSWORD='SUA_SENHA'
mvn spring-boot:run
```

Não confirme senhas reais em Git. O valor `api123` existe apenas para laboratório local.

## Execução do projeto

Abra o terminal na pasta que contém este `pom.xml` e execute:

```bash
mvn clean test
mvn spring-boot:run
```

Na primeira vez, o Maven baixa dependências. A aplicação terminou de iniciar quando o log mostrar o Tomcat na porta `8080` e a inicialização concluída. Mantenha esse terminal aberto.

Teste a saúde em outro terminal:

```bash
curl -i http://localhost:8080/api/saude
```

Resposta esperada:

```http
HTTP/1.1 200
Content-Type: application/json

{"status":"UP","servico":"fundamentos-api"}
```

## Swagger e OpenAPI

Com a aplicação em execução, abra no navegador:

```text
http://localhost:8080/swagger-ui.html
```

Essa é a rota principal do Swagger UI. Por ela é possível visualizar os endpoints, preencher parâmetros, enviar JSON e executar chamadas com o botão `Try it out`.

O contrato OpenAPI também está disponível diretamente:

| Formato | Rota |
| --- | --- |
| JSON | `http://localhost:8080/v3/api-docs` |
| YAML | `http://localhost:8080/v3/api-docs.yaml` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |

Se a aplicação estiver usando outra porta, substitua `8080`. Por exemplo: `http://localhost:8081/swagger-ui.html`.

## Importar a collection no Postman

A collection pronta está em:

```text
postman/Fundamentos_API_Java.postman_collection.json
```

Para importar:

1. Abra o Postman.
2. Clique em `Import`.
3. Escolha `File`.
4. Selecione `Fundamentos_API_Java.postman_collection.json`.
5. Confirme que a variável da collection `baseUrl` contém `http://localhost:8080`.
6. Execute primeiro `Saúde > Verificar saúde da API`.
7. Execute `Produtos > Criar produto`. O teste da requisição guarda automaticamente o ID retornado na variável `produtoId`.
8. Use as requisições de busca, atualização e exclusão, que reutilizam `produtoId`.

Também é possível importar o contrato diretamente no Postman informando `http://localhost:8080/v3/api-docs`, mas a collection entregue já possui exemplos e variáveis preparados para a aula.

## Teste do CRUD

Criar um produto:

```bash
curl -i -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teclado mecanico","preco":299.90,"ativo":true}'
```

O retorno deve usar `201 Created`, trazer o cabeçalho `Location` e devolver o produto com `id`.

Listar e buscar:

```bash
curl -i http://localhost:8080/api/produtos
curl -i http://localhost:8080/api/produtos/1
```

Atualizar:

```bash
curl -i -X PUT http://localhost:8080/api/produtos/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teclado mecanico ABNT2","preco":319.90,"ativo":true}'
```

Excluir:

```bash
curl -i -X DELETE http://localhost:8080/api/produtos/1
```

O retorno da exclusão deve usar `204 No Content`. O arquivo `requests.http` reúne as mesmas chamadas e funciona em IDEs com cliente HTTP compatível.

## Conferência no pgAdmin

1. Atualize `Databases` e abra `api_fundamentos`.
2. Navegue por `Schemas`, `public`, `Tables` e `produtos`.
3. Clique com o botão direito em `produtos` e escolha `View Edit Data` e `All Rows`.
4. Outra opção é abrir o Query Tool do banco `api_fundamentos` e executar:

```sql
SELECT id, nome, preco, ativo FROM produtos ORDER BY id;
```

A tabela nasce na primeira inicialização porque a aula usa `spring.jpa.hibernate.ddl-auto=update`. Em produção, prefira migrações versionadas com Flyway ou Liquibase.

## Geração e execução do JAR

```bash
mvn clean package
java -jar target/fundamentos-api-1.0.0.jar
```

Para escolher outra porta:

```bash
SERVER_PORT=8081 java -jar target/fundamentos-api-1.0.0.jar
```

No PowerShell:

```powershell
$env:SERVER_PORT='8081'
java -jar target/fundamentos-api-1.0.0.jar
```

## Endpoints

| Método | Rota | Resultado esperado |
| --- | --- | --- |
| GET | `/api/saude` | `200 OK` e estado do serviço |
| GET | `/api/produtos` | `200 OK` e lista |
| GET | `/api/produtos/{id}` | `200 OK` ou `404 Not Found` |
| POST | `/api/produtos` | `201 Created` |
| PUT | `/api/produtos/{id}` | `200 OK` ou `404 Not Found` |
| DELETE | `/api/produtos/{id}` | `204 No Content` ou `404 Not Found` |

## Estrutura do código

```text
src/main/java/br/edu/fiap/api
├── FundamentosApiApplication.java
├── erro
│   └── ApiExceptionHandler.java
├── produto
│   ├── aplicacao
│   │   └── ProdutoService.java
│   ├── dominio
│   │   └── Produto.java
│   ├── excecao
│   │   └── ProdutoNaoEncontradoException.java
│   ├── infraestrutura
│   │   └── ProdutoRepository.java
│   └── web
│       ├── ProdutoController.java
│       └── dto
│           ├── ProdutoRequest.java
│           └── ProdutoResponse.java
└── saude
    └── SaudeController.java
```

### Responsabilidade de cada camada

| Camada | Pacote | Responsabilidade |
| --- | --- | --- |
| Web | `produto.web` | Traduz método, URI, cabeçalhos e JSON para chamadas Java |
| DTO | `produto.web.dto` | Define os contratos de entrada e saída sem expor a entidade |
| Aplicação | `produto.aplicacao` | Executa casos de uso e delimita transações |
| Domínio | `produto.dominio` | Mantém o estado e o comportamento do produto |
| Infraestrutura | `produto.infraestrutura` | Persiste dados por meio do Spring Data JPA |
| Exceções | `produto.excecao` e `erro` | Representa falhas esperadas e as converte em respostas HTTP |

O fluxo principal é:

```text
requisição HTTP
      |
      v
ProdutoController  ->  ProdutoService  ->  ProdutoRepository  ->  PostgreSQL
      |                     |                    |
      v                     v                    v
DTO de entrada       regra/transação        persistência JPA
DTO de saída
```

A regra de dependência usada no exemplo é simples: o controller conhece o serviço, o serviço conhece o domínio e o repository, e o repository conhece a entidade. O controller não acessa o banco diretamente.

## JavaDoc do projeto

As classes públicas, os métodos dos casos de uso, os endpoints e os DTOs possuem comentários JavaDoc. Para gerar a documentação HTML:

```bash
mvn javadoc:javadoc
```

Abra o arquivo:

```text
target/reports/apidocs/index.html
```

Para validar a documentação junto com a compilação:

```bash
mvn clean package
mvn javadoc:javadoc
```

## Problemas comuns

### Connection refused em localhost 5432

O serviço PostgreSQL está parado ou usa outra porta. Verifique:

```bash
pg_isready -h localhost -p 5432
```

No Windows, confira o aplicativo `Serviços`. No Linux, use `systemctl status postgresql`. No macOS com Homebrew, use `brew services list`.

### Password authentication failed

A senha em `DB_PASSWORD` não corresponde à senha de `api_user`. Redefina como administrador:

```sql
ALTER ROLE api_user WITH PASSWORD 'api123';
```

### Database api_fundamentos does not exist

Crie o banco conectado ao banco de manutenção `postgres`. Não execute `CREATE DATABASE` dentro de `BEGIN` e `COMMIT`.

### Port 8080 already in use

Encerre o processo antigo ou execute com outra porta:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Maven não encontrado

Instale Maven e reinicie o terminal. Confirme que o diretório `bin` está no `PATH`.

### Unsupported class file ou release version not supported

O Maven usa um JDK incompatível. Confira `mvn -version` e corrija `JAVA_HOME`.

## Encerramento limpo

- Pare a API com `Ctrl+C` no terminal.
- O PostgreSQL pode continuar como serviço local para as próximas aulas.
- Para interromper no Linux: `sudo systemctl stop postgresql`.
- Para interromper no macOS com Homebrew: `brew services stop postgresql@18`.

## Limites didáticos

Este projeto prioriza os fundamentos de HTTP, REST e persistência local. Ele não inclui autenticação, paginação, OpenAPI, migrações, observabilidade ou controle de concorrência. A remoção física e `ddl-auto=update` servem ao laboratório e não representam escolhas automáticas para produção.

## Referências oficiais

- Spring Boot System Requirements: <https://docs.spring.io/spring-boot/system-requirements.html>
- Spring Boot SQL Databases: <https://docs.spring.io/spring-boot/reference/data/sql.html>
- PostgreSQL Downloads: <https://www.postgresql.org/download/>
- pgAdmin Downloads: <https://www.pgadmin.org/download/>
- HTTP Semantics RFC 9110: <https://www.rfc-editor.org/rfc/rfc9110.html>
