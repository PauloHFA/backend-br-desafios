# Desafio 06: Pontos de Interesse por GPS (`points-of-interest`)

Este módulo implementa a solução para o sexto desafio da comunidade [backend-br/desafios](https://github.com/backend-br/desafios/blob/main/points-of-interest/PROBLEM.md).

---

## 🎯 Objetivo
Construir uma API REST para gerenciamento de pontos de interesse (POIs) baseados em coordenadas cartesianas inteiras não-negativas ($X$ e $Y$) e realizar consultas por proximidade espacial baseadas na **Distância Euclidiana**:

$$\text{Distância} = \sqrt{(X_{poi} - X_{ref})^2 + (Y_{poi} - Y_{ref})^2} \le d_{max}$$

---

## 🗺️ Dados Iniciais (Seed)
A aplicação inicializa automaticamente com os pontos do enunciado:
* **Lanchonete** (27, 12)
* **Posto** (31, 18)
* **Joalheria** (15, 12)
* **Floricultura** (19, 21)
* **Pub** (12, 8)
* **Supermercado** (23, 6)
* **Churrascaria** (28, 2)

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

A API estará disponível na porta `8085` (`http://localhost:8085`).

---

## 📡 Endpoints

### 1. Cadastrar Ponto de Interesse
`POST /points-of-interest`
```json
{
  "name": "Academia Central",
  "x": 10,
  "y": 15
}
```
**Resposta (201 Created):**
```json
{
  "id": 8,
  "name": "Academia Central",
  "x": 10,
  "y": 15
}
```

### 2. Listar Todos os Pontos de Interesse
`GET /points-of-interest?page=0&size=20`

### 3. Consultar por Proximidade
`GET /points-of-interest/nearby?x=20&y=10&dmax=10`

**Resposta (200 OK):**
```json
[
  {
    "id": 6,
    "name": "Supermercado",
    "x": 23,
    "y": 6,
    "distance": 5.0
  },
  {
    "id": 3,
    "name": "Joalheria",
    "x": 15,
    "y": 12,
    "distance": 5.39
  },
  {
    "id": 1,
    "name": "Lanchonete",
    "x": 27,
    "y": 12,
    "distance": 7.28
  },
  {
    "id": 5,
    "name": "Pub",
    "x": 12,
    "y": 8,
    "distance": 8.25
  }
]
```
*(Os resultados são filtrados diretamente no banco de dados e ordenados por proximidade).*
