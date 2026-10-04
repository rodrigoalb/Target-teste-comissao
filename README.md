# sales-commission

API Spring Boot que lê as vendas do time comercial e calcula a comissão de cada vendedor.

## Regras

| Valor da venda | Comissão |
| --- | --- |
| Menor que R$ 100,00 | 0% |
| De R$ 100,00 até R$ 499,99 | 1% |
| A partir de R$ 500,00 | 5% |

## Como executar

Execute esse comando no terminal

```bash
.\mvnw.cmd spring-boot:run
```

A API sobe na porta **8081**.

O Maven Wrapper (`mvnw.cmd`) baixa o Maven automaticamente. Não é preciso instalar Maven no computador, mas o Java 21 precisa estar instalado (`JAVA_HOME` apontando para o JDK 21).```

## Endpoints

- `GET /api/comissoes` — calcula comissão usando o arquivo `src/main/resources/vendas.json`
- `POST /api/comissoes` — recebe o JSON de vendas no corpo da requisição

Exemplo:

```bash
curl http://localhost:8081/api/comissoes
```
