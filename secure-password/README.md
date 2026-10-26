# Desafio 04: Validador de Senha Segura (`secure-password`)

Este módulo implementa a solução para o quarto desafio da comunidade [backend-br/desafios](https://github.com/backend-br/desafios/blob/main/secure-password/PROBLEM.md).

---

## 🎯 Objetivo
Implementar um serviço para validar se uma senha atende aos requisitos de segurança estipulados.
* Se a senha atender a todos os critérios: retornar `HTTP 204 No Content`.
* Se não atender: retornar `HTTP 400 Bad Request` com a lista detalhada dos critérios não satisfeitos.

---

## 📋 Critérios de Aceitação da Senha

1. **Comprimento:** Pelo menos **8 caracteres**.
2. **Letra Maiúscula:** Pelo menos uma letra maiúscula (`[A-Z]`).
3. **Letra Minúscula:** Pelo menos uma letra minúscula (`[a-z]`).
4. **Dígito Numérico:** Pelo menos um dígito numérico (`[0-9]`).
5. **Caractere Especial:** Pelo menos um caractere especial (ex: `! @ # $ % & *`).

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

A API estará disponível na porta `8083` (`http://localhost:8083`).

---

## 📡 Endpoint

### Validar Senha
`POST /validate-password`

#### Exemplo de Requisição (Senha Válida):
```json
{
  "password": "vYQIYxO&p$yfI^r"
}
```
**Resposta:** `204 No Content`

#### Exemplo de Requisição (Senha Fraca):
```json
{
  "password": "fraca"
}
```
**Resposta (400 Bad Request):**
```json
{
  "timestamp": "2026-10-26T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "A senha fornecida nao atende a todos os criterios de seguranca.",
  "failures": [
    "A senha deve possuir pelo menos 8 caracteres.",
    "A senha deve conter pelo menos uma letra maiúscula.",
    "A senha deve conter pelo menos um dígito numérico.",
    "A senha deve conter pelo menos um caractere especial (ex: ! @ # $ %)."
  ]
}
```
