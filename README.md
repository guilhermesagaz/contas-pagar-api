# Sistema de Contas a Pagar - API REST

Este projeto é uma API REST simples para gerenciamento de contas a pagar, como parte de um desafio backend. A aplicação permite o CRUD de contas a pagar, alteração de situação, consulta de contas cadastradas com filtros e importação de contas via CSV.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **PostgreSQL**
- **Docker e Docker Compose**
- **Flyway** (para gerenciamento de migrações de banco de dados)
- **JPA/Hibernate** (para mapeamento objeto-relacional)
- **Spring Security** (para autenticação)
- **RabbitMQ** (para filas e processamento assíncrono)
- **JUnit e Mockito** (para testes unitários)
- **OpenCSV** (para importação de CSV)

## Funcionalidades

### 1. CRUD de Contas a Pagar
- **Cadastrar Conta:** Endpoint para cadastrar uma nova conta a pagar.
- **Atualizar Conta:** Atualiza os dados de uma conta já existente.
- **Alterar Situação:** Modifica o status de uma conta para `PENDENTE`, `PAGA` ou `CANCELADA`.
- **Consultar Contas:** Permite buscar uma lista de contas a pagar com filtros por data de vencimento e descrição.
- **Consultar Conta por ID:** Retorna uma conta específica com base no seu ID.
- **Consultar Valor Total Pago:** Calcula o valor total pago em um determinado período.

### 2. Importação de CSV (Assíncrono com RabbitMQ)
- O arquivo CSV é enviado via API, e a importação é processada de forma assíncrona utilizando RabbitMQ.
- O sistema coloca a importação na fila e processa as contas em segundo plano.

### 3. Autenticação
- Mecanismo de autenticação via JWT (ou outro mecanismo escolhido), garantindo que apenas usuários autenticados possam acessar os endpoints.

## Como Executar o Projeto

### Pré-requisitos
- **Docker** e **Docker Compose** instalados.

### Passos

1. Clone o repositório:
    ```bash
    git clone https://github.com/guilhermesagaz/contas-pagar-api.git
    ```

2. Navegue até o diretório do projeto:
    ```bash
    cd docker
    ```

3. Execute o Docker Compose para subir os containers (aplicação, banco de dados, etc.):
    ```bash
    docker-compose up -d
    ```

4. A aplicação estará disponível em:
    ```
    http://localhost:9000
    ```
