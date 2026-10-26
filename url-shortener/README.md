# Desafio 05: Encurtador de URLs (`url-shortener`)

Este módulo implementa a solução para o quinto desafio da comunidade [backend-br/desafios](https://github.com/backend-br/desafios/blob/main/url-shortener/PROBLEM.md).

---

## 🎯 Objetivo
Desenvolver uma API REST para encurtar links longos em identificadores compactos e fáceis de compartilhar:
* O código encurtado possui entre **5 e 10 caracteres alfanuméricos** (`[a-zA-Z0-9]`).
* Cada link é salvo no banco de dados com prazo de validade configurável (ex: 7 dias).
* Ao acessar `GET /{code}`, a API redireciona o cliente via `HTTP 302 Found` para a URL original.
* Caso o link não exista ou esteja expirado, retorna `HTTP 404 Not Found`.

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

A API estará disponível na porta `8084` (`http://localhost:8084`).

---

## 📡 Endpoints

### 1. Encurtar URL
`POST /shorten-url`

#### Exemplo de Requisição:
```json
{
  "url": "https://backendbrasil.com.br"
}
```

#### Exemplo de Resposta (200 OK):
```json
{
  "url": "http://localhost:8084/DXB6V1"
}
```

### 2. Redirecionamento
`GET /{code}` (ex: `GET http://localhost:8084/DXB6V1`)

* **Resposta (302 Found):**
  * Header `Location: https://backendbrasil.com.br`
* **Se o código não existir ou estiver expirado:**
  * **Resposta:** `404 Not Found`
