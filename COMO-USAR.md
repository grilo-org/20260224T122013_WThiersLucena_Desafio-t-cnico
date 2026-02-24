# Coupon API — Como compilar e consumir

## Pré-requisitos

- **Java 23** (JAVA_HOME configurado)
- **Maven** (ou use o wrapper do projeto: `mvnw.cmd`)
- **Docker** e **Docker Compose** (opcional, só para rodar em container)

---

## Como compilar o projeto

Na pasta do projeto (`api`):

```bash
.\mvnw.cmd clean verify
```

- **`clean`** — limpa o diretório `target`
- **`verify`** — compila, roda os testes e gera o relatório de cobertura (JaCoCo)

Para apenas compilar e gerar o JAR, sem rodar testes:

```bash
.\mvnw.cmd clean package -DskipTests
```

O JAR gerado fica em: `target\api-0.0.1-SNAPSHOT.jar`.

---

## Relatório de cobertura (JaCoCo)

O relatório de cobertura é gerado **apenas depois de rodar os testes**. O JaCoCo está configurado no `pom.xml` e dispara na fase `test`.

**Como gerar o relatório:**

```bash
.\mvnw.cmd clean test
```

ou

```bash
.\mvnw.cmd clean package
```

**Onde abrir o relatório:**

| Caminho (a partir da pasta do projeto `api`) | Descrição |
|---------------------------------------------|-----------|
| **`target\site\jacoco\index.html`**         | Relatório HTML da cobertura — abra este arquivo no navegador |

- A pasta `target` é criada pelo Maven na raiz do projeto (`api`).
- O caminho completo no seu PC será algo como:  
  `c:\Users\wanderson.thiers\Desktop\x1\api\target\site\jacoco\index.html`

**Se a pasta não existir:** rode `.\mvnw.cmd clean test` (ou `clean package`) com **JAVA_HOME** apontando para o JDK 23. Só depois disso a pasta `target\site\jacoco` e o `index.html` serão criados.

---

## Como rodar a aplicação

### Opção 1 — Local (Java)

```bash
.\mvnw.cmd spring-boot:run
```

Ou executando o JAR:

```bash
java -jar target\api-0.0.1-SNAPSHOT.jar
```

A API sobe em: **http://localhost:8080**

### Opção 2 — Docker

```bash
docker-compose up --build
```

A API também fica em: **http://localhost:8080**

---

## Rotas da API

| Método | Rota              | Descrição                    |
|--------|-------------------|------------------------------|
| POST   | `/coupons`        | Criar cupom                  |
| DELETE | `/coupons/{id}`   | Deletar cupom (soft delete)  |

Base URL: **http://localhost:8080**

---

## Como consumir as APIs

### 1. Criar cupom — `POST /coupons`

**URL:** `http://localhost:8080/coupons`  
**Content-Type:** `application/json`

**Corpo (exemplo):**

```json
{
  "code": "ABC123",
  "description": "Cupom de desconto 10%",
  "discountValue": 1.50,
  "expirationDate": "2026-12-31T23:59:59",
  "published": false
}
```

**Regras:**  
- `code`: alfanumérico; caracteres especiais são removidos; deve resultar em **6 caracteres**.  
- `discountValue`: mínimo **0.5**.  
- `expirationDate`: não pode ser data passada.  
- `published`: opcional (default `false`).

**Exemplo com curl:**

```bash
curl -X POST http://localhost:8080/coupons ^
  -H "Content-Type: application/json" ^
  -d "{\"code\":\"PROMO1\",\"description\":\"Promoção\",\"discountValue\":2.0,\"expirationDate\":\"2026-12-31T23:59:59\",\"published\":false}"
```

**Resposta 201 (exemplo):**

```json
{
  "id": "uuid-do-cupom",
  "code": "PROMO1",
  "description": "Promoção",
  "discountValue": 2.0,
  "expirationDate": "2026-12-31T23:59:59",
  "status": "ACTIVE",
  "published": false,
  "redeemed": false
}
```

---

### 2. Deletar cupom — `DELETE /coupons/{id}`

**URL:** `http://localhost:8080/coupons/{id}`  
Substitua `{id}` pelo UUID retornado na criação do cupom.

**Exemplo com curl:**

```bash
curl -X DELETE http://localhost:8080/coupons/SEU-UUID-AQUI
```

- **204 No Content** — cupom deletado (soft delete).  
- **400** — cupom já foi deletado.  
- **404** — cupom não encontrado.

---

## Swagger (documentação interativa)

Com a aplicação rodando:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
  Interface para testar as rotas direto no navegador.

- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)  
  Especificação da API em JSON.

No Swagger UI você pode executar **POST /coupons** e **DELETE /coupons/{id}** e ver os schemas de request e response.

---

## Resumo rápido

| Ação           | Comando / URL |
|----------------|----------------|
| Compilar + testar | `.\mvnw.cmd clean verify` |
| Gerar relatório JaCoCo | `.\mvnw.cmd clean test` (depois abra `target\site\jacoco\index.html`) |
| Rodar local   | `.\mvnw.cmd spring-boot:run` |
| Rodar Docker  | `docker-compose up --build` |
| Criar cupom   | POST `http://localhost:8080/coupons` |
| Deletar cupom | DELETE `http://localhost:8080/coupons/{id}` |
| Documentação  | http://localhost:8080/swagger-ui.html |
