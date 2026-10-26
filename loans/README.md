# Desafio 03: Concessão de Empréstimos (`loans`)

Este módulo implementa a solução para o terceiro desafio da comunidade [backend-br/desafios](https://github.com/backend-br/desafios/blob/main/loans/PROBLEM.md).

---

## 🎯 Objetivo
Implementar um serviço para avaliar a concessão e as taxas de juros de diferentes modalidades de crédito com base na idade, renda e localização do cliente.

---

## 📋 Modalidades e Regras de Negócio

| Modalidade | Identificador | Taxa de Juros | Regras de Elegibilidade |
| :--- | :--- | :---: | :--- |
| **Pessoal** | `PERSONAL` | 4% | Salário $\le$ R$ 3.000 **OU** se tiver menos de 30 anos e residir em SP |
| **Garantia** | `GUARANTEED` | 3% | Salário $\le$ R$ 3.000 **OU** se tiver menos de 30 anos e residir em SP |
| **Consignado** | `CONSIGNMENT` | 2% | Salário $\ge$ R$ 5.000 |

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

A API estará disponível na porta `8082` (`http://localhost:8082`).

---

## 📡 Endpoint

### Avaliar Empréstimos do Cliente
`POST /customer-loans`

#### Exemplo de Requisição:
```json
{
  "age": 26,
  "cpf": "275.484.389-23",
  "name": "Vuxaywua Zukiagou",
  "income": 7000.00,
  "location": "SP"
}
```

#### Exemplo de Resposta (200 OK):
```json
{
  "customer": "Vuxaywua Zukiagou",
  "loans": [
    {
      "type": "PERSONAL",
      "interest_rate": 4
    },
    {
      "type": "GUARANTEED",
      "interest_rate": 3
    },
    {
      "type": "CONSIGNMENT",
      "interest_rate": 2
    }
  ]
}
```
