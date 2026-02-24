Desafio técnico

# Coupon API

API REST para gestão de cupons de desconto. Spring Boot 3, Java 17, H2 em memória, documentação com Swagger.

## Pré-requisitos

- **Java 17+** (JAVA_HOME configurado)
- **Maven** (ou wrapper do projeto: `mvnw` / `mvnw.cmd`)
- **Docker** e **Docker Compose** (opcional, para rodar em container)

## Build

```bash
./mvnw clean verify
```

No Windows: `.\mvnw.cmd clean verify`

## Executar

**Local:**

```bash
./mvnw spring-boot:run
```

**Docker:**

```bash
docker compose up --build
```

A aplicação sobe em **http://localhost:8080**.

## URLs úteis

| Recurso      | URL |
|-------------|-----|
| API         | http://localhost:8080 |
| Swagger UI  | http://localhost:8080/swagger-ui.html |
| H2 Console  | http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:coupondb`, user: `sa`, senha em branco) |

## Principais rotas

| Método | Rota | Descrição |
|--------|------|-----------|
| POST   | `/coupons`       | Criar cupom |
| DELETE | `/coupons/{id}`  | Deletar cupom (soft delete) |

## Swagger 
http://localhost:8080/swagger-ui/index.html#/
<img width="1329" height="505" alt="image" src="https://github.com/user-attachments/assets/3d9b2660-d3fb-4bcb-9bba-a51d51344aab" />


## Cobertura dos testes Jacoco
## Relatório HTML da cobertura — abra este arquivo no navegador
<img width="1358" height="453" alt="image" src="https://github.com/user-attachments/assets/c388e2fe-39d4-4d63-88c0-f6903724674d" />

## Rotas da API http://localhost:8080

| Método | Rota              | Descrição                    |
|--------|-------------------|------------------------------|
| POST   | `/coupons`        | Criar cupom                  |
| DELETE | `/coupons/{id}`   | Deletar cupom (soft delete)  |


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




