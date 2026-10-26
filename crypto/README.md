# Desafio 01: Criptografia Transparente (`cryptography`)

Este módulo implementa a solução para o primeiro desafio da comunidade [backend-br/desafios](https://github.com/backend-br/desafios/blob/main/cryptography/PROBLEM.md).

---

## 🎯 Objetivo
Implementar a criptografia de campos sensíveis (`userDocument` e `creditCardToken`) em um serviço de CRUD de forma **transparente** para as camadas de API e de regras de negócio.

---

## 🔒 Arquitetura de Segurança
* **Algoritmo:** **AES-256 no modo GCM (Galois/Counter Mode)** com `NoPadding`.
* **Segurança:** O modo GCM provê criptografia autenticada (AEAD), garantindo tanto o sigilo quanto a integridade do dado. Para cada operação de escrita, um **Vetor de Inicialização (IV)** criptograficamente seguro de 12 bytes é gerado aleatoriamente e acoplado ao texto cifrado antes da codificação Base64.
* **Transparência JPA:** Foi criado o `EncryptedStringConverter` que implementa `jakarta.persistence.AttributeConverter<String, String>`:
  * `convertToDatabaseColumn`: Converte o texto claro da entidade para a cifra antes de gravar no banco de dados.
  * `convertToEntityAttribute`: Decifra o valor armazenado no banco ao carregar a entidade.

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

A API estará disponível em: `http://localhost:8080`  
Console H2 (para inspeção do banco em memória): `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:cryptodb`, User: `sa`, Senha em branco).

---

## 📡 Endpoints da API

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/operations` | Cria uma nova operação |
| `GET` | `/api/operations/{id}` | Busca operação por ID |
| `GET` | `/api/operations` | Lista operações com paginação |
| `PUT` | `/api/operations/{id}` | Atualiza uma operação existente |
| `DELETE` | `/api/operations/{id}` | Remove uma operação |

### Exemplo de Requisição (POST `/api/operations`)
```json
{
  "userDocument": "123.456.789-00",
  "creditCardToken": "tok_visa_4242_super_secret",
  "value": 15000
}
```

### O que o banco de dados armazena:
```sql
SELECT id, user_document, credit_card_token, operation_value FROM tb_operations;
```
As colunas `user_document` e `credit_card_token` conterão valores cifrados em Base64 (ex: `Q2hlY2tJdlJ...`), enquanto na API a resposta sempre será entregue no formato legível original.
