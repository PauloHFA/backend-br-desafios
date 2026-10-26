# Desafio 02: Autenticação com Token (`authentication`)

Este módulo implementa a solução para o segundo desafio da comunidade [backend-br/desafios](https://github.com/backend-br/desafios/blob/main/authentication/PROBLEM.md).

---

## 🎯 Objetivo
Implementar um serviço que valide um token de acesso recebido no header `Authorization` da requisição HTTP:
* Se válido: retornar `HTTP 204 No Content`
* Se inválido ou ausente: retornar `HTTP 401 Unauthorized` com detalhes claros do erro em JSON.
* A validação deve interceptar a requisição antes de atingir o controller e continuar funcionando de forma transparente para novos endpoints protegidos.

---

## 🔒 Arquitetura de Segurança
* **Filtro de Interceptação:** `JwtAuthenticationFilter` (estende `OncePerRequestFilter`), intercepta todas as requisições antes de chegarem à camada de controle.
* **Mecanismo de Token:** JWT assinado com algoritmo **HMAC-SHA256** utilizando `java-jwt`.
* **Tratamento de Não Autorizados:** `JwtAuthenticationEntryPoint` formata a resposta com status 401 e payload estruturado em JSON.
* **Compatibilidade:** O filtro aceita o token com ou sem o prefixo `Bearer `.

---

## 🚀 Como Executar

### Pré-requisitos
* Java 17+
* Maven 3.8+

### Executar os Testes Automatizados
```bash
mvn test
```

### Iniciar a Aplicação
```bash
mvn spring-boot:run
```

A API estará disponível na porta `8081` (`http://localhost:8081`).

---

## 📡 Endpoints

### 1. Obter Token (Login)
`POST /api/auth/login`
```json
{
  "username": "admin",
  "password": "admin123"
}
```
**Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresInMinutes": 60
}
```

### 2. Endpoint Protegido do Desafio
`GET /foo-bar`

* **Com token válido no Header:**
  `Authorization: Bearer <SEU_TOKEN>`
  **Resposta:** `204 No Content`

* **Com token inválido ou ausente:**
  **Resposta (401 Unauthorized):**
  ```json
  {
    "timestamp": "2026-10-26T12:00:00Z",
    "status": 401,
    "error": "Unauthorized",
    "message": "Token de acesso invalido ou ausente.",
    "path": "/foo-bar"
  }
  ```

### 3. Endpoint Público
`GET /api/public/ping`  
Retorna `200 OK` sem necessidade de token.
