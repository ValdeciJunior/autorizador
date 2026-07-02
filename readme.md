# Mini-Autorizador Transacional

Projeto de autorização de transações de voucher de benefício 
desenvolvido em Java 21 com Spring Boot, focado em 
consistência de dados sob cenários concorrência. Utiliza também AI para analisar
transações suspeitas de fraude.
A aplicação simula um ecossistema distribuído de alta performance e 
utiliza travas para mitigar problemas de 
concorrência (Race Conditions).

## Arquitetura e Tecnologias

* **Java 21** & **Spring Boot 3.x**
* **Spring Data JPA** com Lock Pessimista (`PESSIMISTIC_WRITE`)
* **Spring AI** integrando com um agente do Grok
* **MySQL 5.7** como banco de dados relacional
* **Flyway** para migrações automatizadas de schema
* **Docker & Docker Compose** para orquestração multi-instância em ambiente de desenvolvimento
* **K6** para testes de estresse e validação de carga concorrente

---

## Como Executar o Ambiente Distribuído

A aplicação está configurada para subir de forma totalmente automatizada via Docker. 
O `docker-compose.yml` faz o build do projeto utilizando o 
Gradle interno do contêiner e distribui a aplicação em **3 instâncias fixas** 
espelhadas (um load Balancing manual para testes), rodando nas portas 
locais `8080`, `8081` e `8082`.

### 1. Subir o ambiente do zero (Compilando e aplicando Migrations do flyway)
Para subir a aplicação com as 3 instâncas nas portas `8080`, `8081`, 
`8082` com o banco de dados:
```bash
docker compose up -d --build
```
Assim também vai executar as migrations do flyway criando assim as tabelas 
automaticamente e populando com dados iniciais para os testes devidos.

*Obs: Caso precise alterar as portas estáticamente definidas, alterar o
arquivo ```docker-compose.yml```*

### 2. Derrubar a aplicação(e apagando completamente a infra do projeto)
Para derrubar a aplicação e assim a infra(as instâncias e a estrutura do banco):
```bash
docker compose down -v
```
Assim você reinicia do zero, para poder subir novamente com o comando do
passo 1 para e voltar o banco para o estado inicial e poder refazer
os testes.

Para derrubar apenas a aplicação sem apagar a infra:
```bash
docker compose down
```

### 3. Executar o teste do K6 estressar a aplicação e simular uma concorrência
Para executar o teste com o K6:
```bash
docker run --rm -i -v "${PWD}:/scripts" loadimpact/k6 run /scripts/teste-estresse.js
```
Vai ser feito o download da imagem e o teste vai ser executado.

Caso precise fazer alguma alteração nos teste, basta alterar o arquivo ```teste-estresse.js```.
