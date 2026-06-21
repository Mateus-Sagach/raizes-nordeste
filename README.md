# Raízes do Nordeste — API Back-end

API REST para gestão de pedidos da rede ficticia de lanchonetes Raízes do Nordeste. Desenvolvida com Spring Boot 

---

## Tecnologias utilizadas

- Java 17+
- Spring Boot 3.5.14
- Spring Security com JWT
- Spring Data JPA com Hibernate
- PostgreSQL 16
- Flyway (migrations)
- Springdoc OpenAPI 2.8.8 (Swaggger)
- Lombok
- Maven

---

## Pré-requisitos

Antes de rodar o projeto, é preciso ter instalado:

- [Java 17 ou superior](https://adoptium.net)
- [PostgreSQL 16](https://www.postgresql.org/download)
- [Git](https://git-scm.com/download/win)

---

## Como configurar e rodar

### 1. Clone o repositório

```bash
git clone https://github.com/Mateus-Sagach/raizes-nordeste.git
cd raizes-nordeste
```

### 2. Crie o banco de dados

Abra o terminal e acesse o PostgreSQL:

```bash
psql -U postgres
```

Crie o banco:

```sql
CREATE DATABASE raizes_nordeste;
\q
```

### 3. Configure as variáveis de ambiente

Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

Abra o arquivo `.env` e preencha com suas credenciais:

```env
DB_URL=jdbc:postgresql://localhost:5432/raizes_nordeste
DB_USER=postgres
DB_PASS=sua_senha_do_postgres
JWT_SECRET=raizes-nordeste-chave-secreta-minimo-256-bits-2026
```

### 4. Configure o .env no IntelliJ
Run -> Edit Configurations
-> Selecione NordesteApplication
-> Environment variables -> Load from file -> selecione .env
-> Apply -> OK

### 5. Instale as dependências e rode

```bash
mvn spring-boot:run
```

Ou pelo IntelliJ clique no botão Run.

As migrations do Flyway rodam automaticamente.
Você vai ver no console:
Successfully applied 8 migrations
Started NordesteApplication in X seconds

---

## Como acessar a documentação

Com a aplicação rodando, acesse:
http://localhost:8080/swagger-ui.html

A documentação interativa mostra todos os endpoints
com exemplos de request e response.

Para autenticar no Swagger:
1. Faça login em `POST /auth/login`
2. Copie o `accessToken` da resposta
3. Clique em **Authorize** no topo da página
4. Cole o token e clique em **Authorize**

---

## Credenciais de teste

Após rodar as migrations, o banco já possui dados
de teste prontos para usar.

Para criar usuários de teste, use o endpoint:
`POST /auth/cadastro`

Exemplo de usuário CLIENTE:
```json
{
  "nome": "Cliente Teste",
  "email": "cliente@teste.com",
  "senha": "123456",
  "perfil": "CLIENTE",
  "consentimentoLgpd": true
}
```

Exemplo de usuário GERENTE:
```json
{
  "nome": "Gerente Teste",
  "email": "gerente@teste.com",
  "senha": "123456",
  "perfil": "GERENTE",
  "consentimentoLgpd": false
}
```

---

## Fluxo crítico de teste

Sequência recomendada para testar o fluxo principal:

### 1. Cadastrar e fazer login
POST /auth/cadastro    <-  cria o usuario
POST /auth/login       <- obtem o token JWT

### 2. Criar um pedido
```json
POST /pedidos
{
  "unidadeId": 1,
  "canalPedido": "APP",
  "itens": [{"produtoId": 1, "quantidade": 1}],
  "formaPagamento": "MOCK"
}
```

### 3. Acompanhar  o status
GET /pedidos/{pedidoId}

Regra do pagamento mock:
- Pedidos com ID par: pagamento  aprovado -> status EM_PREPARACAO
- Pedidos com ID impar: pagamento recusado  -> status AGUARDANDO_PAGAMENTO

---

## Como rodar os testes com Postman

### 1. Importe os arquivos

No Postman, clique em "Import" e selecione os dois arquivos:

- postman/raizes-nordeste.postman_collection.json
- postman/Local.postman_environment.json


### 2. Selecione o ambiente "Local"

No canto superior direito do Postman, no dropdown de ambientes,
selecione "Local" (foi importado junto com o arquivo acima).

Variáveis já configuradas:
- base_url  = http://localhost:8080

Variáveis preenchidas automaticamente durante a execução dos testes:
- token, token_cliente , token_gerente
- pedidoId, pedidoIdStatus
- email_cliente_teste , email_duplicado_teste, email_gerente_teste

### 3. Execute na ordem recomendada

01 - Auth        -> executa  primeiro para salvar os tokens
02 - Pedidos      -> usa token_cliente  e token_gerente
03 - Estoque      -> usa o token_gerente
04 - Fidelidade   -> usa  token_cliente
05 - Erros        -> testa cenários   negativos
06 - Cardapio        -> usa o token_cliente

### 4. Para rodar todos de uma vez

Clique com botão direito na collection -> Run collection -> Delay: 500ms -> Run

---

## Estrutura do projeto
```
src/main/java/com/raizes/nordeste/
├── domain/
│   ├── model/        <- entidades JPA com regras  de negocio
│   └── enums/        <- tipos enumerados 
├── application/
│   ├── auth/         <- autenticacao e cadastro
│   ├── pedido/       <- fluxo critico  de pedidos
│   ├── pagamento/    <-  integracao  mock de pagamento
│   ├── estoque/      <- controle de  estoque  por unidade
│   ├── fidelidade/   <- programa de  pontos
│   └── audit/        <- auditoria de acoes sensiveis
├── infrastructure/
│   ├── repository/   <-  acesso ao banco de  dados
│   ├── security/     <- JWT, filtros e configuracao
│   ├── gateway/      <-  integracao  externa simulada
│   └── config/       <- configuracoes do Swagger
└── api/
    ├── controller/   <- endpoints  REST
    ├── dto/          <- objetos  de entrada  e saida
    └── exception/    <- tratamento de erros  padronizado

src/main/resources/
├── application.yml              <-  configuracao da aplicacao
└── db/migration/                <- scripts  SQL do Flyway
    ├── V1__criar_usuarios.sql
    ├── V2__criar_unidades_produtos.sql
    ├── V3__criar_pedidos.sql
    ├── V4__criar_pagamentos.sql
    ├── V5__criar_estoque.sql
    ├── V6__criar_fidelidade_audit.sql
    ├── V7__seed_dados_iniciais.sql
    ├── V8__corrigir_tipo_estado.sql
    └── V9__corrigir_senhas_seed.sql
```
---

## Endpoints principais

| Método | Rota | Descrição | Perfil |
|--------|------|-----------|--------|
| POST | /auth/login | Autentica e  retorna token | Público |
| POST | /auth/cadastro | Cadastra   novo usuario | Público |
| GET | /auth/me | Dados do  usuario logado | Todos |
| POST | /pedidos | Cria pedido com  canalPedido | CLIENTE ,  ATENDENTE |
| GET | /pedidos | Lista pedidos com filtros | GERENTE,  ADMIN |
| GET | /pedidos/{id} | Detalha  um pedido | Autenticado |
| PATCH | /pedidos/{id}/status | Atualiza status | COZINHA, GERENTE |
| DELETE | /pedidos/{id} | Cancela  pedido | CLIENTE, GERENTE |
| GET | /estoque | Consulta estoque da unidade | GERENTE, ATENDENTE |
| POST | /estoque/entrada | Registra  entrada | GERENTE, ADMIN |
| GET | /fidelidade/me | Saldo  de pontos | CLIENTE |
| GET | /fidelidade/me/historico | Historico  de pontos | CLIENTE |

---

## Segurança e LGPD

- Senhas armazenadas com BCrypt (hash irreversivel)
- Autenticacao  via JWT com expiracao de 1 hora
- Autorizacao por perfis: CLIENTE, ATENDENTE, COZINHA,
  GERENTE e ADMIN
- Acumulo de pontos so com consentimento da LGPD ativo
- Respostas nunca expoem senhaHash ou dados  sensiveis
- Auditoria de  acoes sensiveis em audit_log
- Direito ao esquecimento  via anonimizarDados()

---

## Observações técnicas

**Campo canalPedido:** todo pedido deve informar o canal
de origem (APP, TOTEM, BALCAO, PICKUP, WEB). Campo
obrigatorio .

**Pagamento mock:** o sistema nao processa pagamentos
reais. Pedidos com ID par sao aprovados automaticamente.
Pedidos com ID impar sao recusados.

**Idempotencia:** se o mesmo pedido for enviado para
pagamento duas vezes, o sistema retorna o pagamento
que sem processar novamente.

**Validacao de estoque:** o estoque e verificado antes
de criar o pedido. Se insuficiente, retorna 409 com
ESTOQUE_INSUFICIENTE antes de qualquer cobrança.

---

## Links

Repositorio:
https://github.com/Mateus-Sagach/raizes-nordeste


Documentacao Swagger (com aplicacao rodando):
http://localhost:8080/swagger-ui.html

