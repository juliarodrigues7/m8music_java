# 🎶 M8MUSIC

> Aplicação desenvolvida em **Java** com **Spring Boot** e **OracleSQL**, focada no gerenciamento de pedidos musicais entre clientes e cantores.
> Integra autenticação JWT, autorização por papéis, mensageria assíncrona com **RabbitMQ**, busca musical via **Spotify API** e documentação com **Swagger**.

---

## 👥 Integrantes do Grupo

| Nome Completo | Função / Responsabilidade |
|---|---|
| **Henrique Batista de Souza - RM99742** | Líder do Projeto / Desenvolvedor Full-Stack (Java & ASP.NET / React.js & React-Native & TypeScript) |
| **Julia Lima Rodrigues - RM559781** | Desenvolvedora Back-end (Java & ASP.NET) / DevOps (Microsoft Azure) / QA & Insurance |
| **Felipe Soares Gonçalves - RM559175** | Desenvolvedor Front-End (React.js) / Desenvolvedor Mobile (React-Native) / Desenvolvedor IoT (Arduino) / Banco de Dados (OracleSQL) |

---

## 🗓️ Cronograma de Desenvolvimento

| Etapa | Atividade | Responsável | Prazo | Status |
|---|---|---|---|---|
| 1 | Coordenação de atividades | Julia | 09/11/2025 | ✅ Concluído |
| 2 | Correção e aplicação do banco de dados para API Java | Felipe | 09/11/2025 | ✅ Concluído |
| 3 | Documentação da API e testes | Julia | 09/11/2025 | ✅ Concluído |
| 4 | Integração com o banco de dados OracleSQL | Henrique | 09/11/2025 | ✅ Concluído |
| 5 | Adequação e HTTP response para controllers | Henrique | 09/11/2025 | ✅ Concluído |
| 6 | Gravação e entrega Sprint 2 | Henrique | 09/11/2025 | ✅ Concluído |
| 7 | Implementação de autenticação JWT com dois perfis de usuário | Henrique | 2026 | ✅ Concluído |
| 8 | Integração com Spotify API via OpenFeign + Resilience4j | Henrique | 2026 | ✅ Concluído |
| 9 | Configuração de mensageria assíncrona com RabbitMQ | Henrique | 2026 | ✅ Concluído |
| 10 | Autorização por roles (CANTOR / CLIENTE) e controle de rotas | Henrique | 2026 | ✅ Concluído |
| 11 | Gravação e entrega Sprint 3 | Henrique | 2026 | ✅ Concluído |

---

## 📈 Evolução entre Sprints

### Sprint 1
- CRUD básico de entidades (Cantor, Cliente, Música, Pedido)
- Conexão com banco de dados OracleSQL
- Documentação inicial com Swagger

### Sprint 2
- Adequação das rotas com **HATEOAS** (links de hipermídia nas respostas)
- Melhoria dos **HTTP status codes** nos controllers
- Correções no mapeamento do banco de dados

### Sprint 3 _(atual)_
- **Autenticação JWT** com dois perfis distintos: `CANTOR` (token válido por 6 dias) e `CLIENTE` (token válido por 6 horas)
- **Autorização por roles** com Spring Security: rotas protegidas por `ROLE_CANTOR` e `ROLE_CLIENTE`
- **Integração com Spotify API** para busca de músicas via OpenFeign com circuit breaker e retry automático (Resilience4j)
- **Mensageria assíncrona com RabbitMQ**: pedidos publicam eventos em fila ao serem criados, com suporte a Dead Letter Queue
- Suporte a **dois perfis de banco de dados**: H2 em memória (dev) e Oracle (produção)
- **Tratativa de erros** centralizada com `GlobalExceptionHandler`
- **Migrações de banco** com Flyway

---

## ⚙️ Como Rodar a Aplicação

### Pré-requisitos

- Java 17+
- Maven 3.8+
- OracleSQL (para perfil de produção) ou H2 (perfil dev, sem instalação)
- RabbitMQ em execução local (porta 5672)
- Credenciais de uma aplicação Spotify (Client ID e Client Secret)

### Passos para execução

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/CyPHER298/m8muisc-api.git
   cd m8music-api
   ```

2. **Configurar as variáveis de ambiente:**

   Crie um arquivo `.env` na raiz do projeto com:
   ```env
   SPOTIFY_CLIENT_ID=seu_spotify_client_id
   SPOTIFY_CLIENT_SECRET=seu_spottify_client_secret
   ```

3. **Executar com perfil de desenvolvimento (H2):**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   **Executar com perfil Oracle (produção):**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=oracle
   ```

4. **Acessar a documentação Swagger:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

5. **Acessar o console H2 (somente perfil dev):**
   ```
   http://localhost:8080/h2-console
   ```

---

## 🔐 Autenticação e Autorização

A API utiliza **JWT (JSON Web Token)** para autenticação stateless. Existem dois tipos de usuário com fluxos distintos:

| Tipo | Registro | Login | Validade do Token |
|---|---|---|---|
| **Cantor** | `POST /auth/register-cantor` | `POST /auth/login` | 6 dias |
| **Cliente** | `POST /auth/register-cliente` | `POST /auth/login` | 6 horas |

Após o login, inclua o token no header de todas as requisições protegidas:
```
Authorization: Bearer <seu_token>
```

### Regras de acesso por rota

| Método | Rota | Acesso |
|---|---|---|
| `GET` | `/api/cantores/**` | Público |
| `POST/PUT/DELETE` | `/api/cantores/**` | `ROLE_CANTOR` |
| `GET` | `/api/musica/**` | Público |
| `POST/PUT/DELETE` | `/api/musica/**` | `ROLE_CANTOR` |
| `POST` | `/api/pedidos/**` | `ROLE_CLIENTE` |
| `GET` | `/api/pedidos/**` | Autenticado |
| `*` | `/api/clientes/**` | Autenticado |
| `*` | `/api/spotify/**` | Autenticado |

---

## 🔗 Endpoints da API

### Autenticação (`/auth`)

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Realiza login e retorna token JWT |
| `POST` | `/auth/register-cantor` | Cadastra um novo cantor |
| `POST` | `/auth/register-cliente` | Cadastra um novo cliente |
| `GET` | `/auth/token` | Gera token a partir da autenticação atual |

### Cantores (`/api/cantores`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/api/cantores` | Lista todos os cantores | Público |
| `GET` | `/api/cantores/{id}` | Retorna um cantor pelo ID | Público |
| `POST` | `/api/cantores` | Cadastra um novo cantor | `CANTOR` |
| `PUT` | `/api/cantores/{id}` | Atualiza um cantor | `CANTOR` |
| `DELETE` | `/api/cantores/{id}` | Remove um cantor | `CANTOR` |

### Clientes (`/api/clientes`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/api/clientes` | Lista todos os clientes | Autenticado |
| `GET` | `/api/clientes/{id}` | Retorna um cliente pelo ID | Autenticado |
| `POST` | `/api/clientes` | Cadastra um novo cliente | Autenticado |
| `PUT` | `/api/clientes/{id}` | Atualiza um cliente | Autenticado |
| `DELETE` | `/api/clientes/{id}` | Remove um cliente | Autenticado |

### Músicas (`/api/musica`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/api/musica` | Lista todas as músicas | Público |
| `GET` | `/api/musica/{id}` | Retorna uma música pelo ID | Público |
| `POST` | `/api/musica` | Cadastra uma nova música | `CANTOR` |
| `PUT` | `/api/musica/{id}` | Atualiza uma música | `CANTOR` |
| `DELETE` | `/api/musica/{id}` | Remove uma música | `CANTOR` |

### Pedidos (`/api/pedidos`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/api/pedidos` | Lista todos os pedidos | Autenticado |
| `GET` | `/api/pedidos/{id}` | Retorna um pedido pelo ID | Autenticado |
| `POST` | `/api/pedidos` | Cria um pedido (publica evento no RabbitMQ) | `CLIENTE` |
| `PUT` | `/api/pedidos/{id}` | Atualiza um pedido | Autenticado |
| `DELETE` | `/api/pedidos/{id}` | Remove um pedido | Autenticado |

### Spotify (`/api/spotify`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/api/spotify/search` | Busca músicas no Spotify | Autenticado |

**Parâmetros de busca Spotify:**

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `q` | String | Termo de busca |
| `type` | String | Tipo (track, artist, album) |
| `limit` | int | Quantidade de resultados |
| `offset` | int | Paginação |
| `market` | String | Mercado (ex: BR) |

---

## 🐇 Mensageria com RabbitMQ

Ao criar um pedido, um evento é publicado automaticamente no RabbitMQ:

| Configuração | Valor |
|---|---|
| Exchange | `m8music.exchange` (Direct) |
| Fila principal | `pedido.queue` |
| Routing Key | `pedido.criado` |
| Dead Letter Queue | `pedido.dlq` |

O consumidor (`PedidoConsumer`) processa os eventos da fila e os registra em log para auditoria.

---

## 🎵 Integração com Spotify

A busca de músicas é feita via **OpenFeign** com autenticação por **Client Credentials OAuth**. A integração inclui:

- **Retry automático** com backoff exponencial (até 3 tentativas)
- **Circuit Breaker** via Resilience4j para evitar falhas em cascata
- **Fallback** que retorna mensagem amigável quando o Spotify está indisponível

---

## 🧩 Diagramas da Aplicação

### MER
![Modelo de Entidade Relacionamento](./docs/mer_bd.jpeg)

### DER
![Diagrama de Entidade Relacionamento](./docs/der_bd.jpeg)

---

## 🧾 Tecnologias Utilizadas

| Tecnologia | Versão   | Uso |
|---|----------|---|
| Java | 17+      | Linguagem principal |
| Spring Boot | 4.0.5    | Framework base |
| Spring Security | Latest   | Autenticação e autorização |
| Spring HATEOAS | Latest   | Hipermídia nas respostas REST |
| Spring AMQP | Latest   | Integração com RabbitMQ |
| Spring Cloud OpenFeign | 2025.1.1 | Clientes HTTP declarativos |
| Resilience4j | Latest   | Circuit breaker e retry |
| Auth0 Java JWT | 4.5.1    | Geração e validação de tokens JWT |
| Flyway | Latest   | Migrations de banco de dados |
| OracleSQL (OJDBC11) | Latest   | Banco de dados de produção |
| H2 | Latest   | Banco de dados em memória (dev) |
| Lombok | Latest   | Redução de boilerplate |
| Springdoc OpenAPI (Swagger) | 3.0.2    | Documentação da API |
| RabbitMQ | Latest   | Mensageria assíncrona |

---

## 🗃️ Script de Criação do Banco de Dados

Caso necessário, utilize o script abaixo para recriar as tabelas:

```sql
DROP TABLE avaliacao CASCADE CONSTRAINTS;
DROP TABLE pedido CASCADE CONSTRAINTS;
DROP TABLE musica CASCADE CONSTRAINTS;
DROP TABLE cantor CASCADE CONSTRAINTS;
DROP TABLE cliente CASCADE CONSTRAINTS;

CREATE TABLE cliente (
  id_cliente NUMBER(2) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nm_cliente VARCHAR2(50) NOT NULL
);

CREATE TABLE cantor (
  id_cantor NUMBER(2) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nm_cantor VARCHAR2(50) NOT NULL,
  senha_cantor VARCHAR2(10),
  email_cantor VARCHAR2(50) UNIQUE
);

CREATE TABLE musica (
  id_musica NUMBER(2) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  titulo VARCHAR2(50) NOT NULL,
  artista VARCHAR2(50),
  genero VARCHAR2(50)
);

CREATE TABLE pedido (
  id_pedido NUMBER(2) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  id_cliente NUMBER(2) NOT NULL,
  id_musica NUMBER(2) NOT NULL,
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  FOREIGN KEY (id_musica) REFERENCES musica(id_musica)
);

CREATE TABLE avaliacao (
  id_avaliacao NUMBER(2) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nota NUMBER CONSTRAINT chk_nota CHECK (nota BETWEEN 1 AND 5),
  id_musica NUMBER(2) NOT NULL,
  id_cliente NUMBER(2) NOT NULL,
  FOREIGN KEY (id_musica) REFERENCES musica(id_musica),
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);
```

---

## 🎥 Vídeo de Apresentação

📺 [Assista à apresentação no YouTube](https://youtu.be/8oGh5lXjscI)

---

## 📜 Observação

Este projeto foi desenvolvido para fins acadêmicos na disciplina de **Desenvolvimento Web — Sprint 4 (Java)** na **FIAP**.
