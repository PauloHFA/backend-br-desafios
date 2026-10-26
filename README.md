# Desafios Backend-BR

Minhas resoluções para os desafios da comunidade [backend-br/desafios](https://github.com/backend-br/desafios), implementados em **Java** com foco em código limpo, arquitetura em camadas e testes automatizados.

## 🎯 Desafios

| # | Desafio | Descrição | Stack | Status |
| :-: | :--- | :--- | :--- | :-: |
| 01 | [`crypto`](./crypto/) | Criptografia / decriptografia transparente de dados sensíveis (AES-256 GCM) | Java 17, Spring Boot 3, JPA Converter, H2 | ✅ Concluído |
| 02 | [`auth`](./auth/) | Validação de token de acesso via filtro interceptor (`OncePerRequestFilter`) | Java 17, Spring Boot 3, Spring Security, JWT | ✅ Concluído |
| 03 | [`loans`](./loans/) | Validação de concessão e regras de empréstimos | Java 17, Spring Boot 3, Bean Validation | ✅ Concluído |
| 04 | [`secure-password`](./secure-password/) | Validador de complexidade e regras de senhas | Java 17, Spring Boot 3, RegEx Engine | ✅ Concluído |
| 05 | [`url-shortener`](./url-shortener/) | Encurtador de URLs, controle de expiração e redirecionamento HTTP 302 | Java 17, Spring Boot 3, JPA, H2 | ✅ Concluído |
| 06 | [`points-of-interest`](./points-of-interest/) | Pontos de interesse por coordenadas GPS | Java 17, Spring Boot 3 | ⏳ A seguir |

---

## 🛠️ Como Clonar e Executar

```bash
git clone https://github.com/PauloHFA/backend-br-desafios.git
cd "backend-br-desafios"
```

Cada desafio é um módulo autônomo com seu próprio `pom.xml` e instruções detalhadas dentro de sua pasta.
