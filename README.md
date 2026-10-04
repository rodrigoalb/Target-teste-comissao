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

Caso já tenha o java 21 instalado no seu computador, execute esse comando para trocar a versão e executar a aplicação

```bash
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
java -version
.\mvnw.cmd clean spring-boot:run
```


## Endpoints

- `GET /api/comissoes` — Retorna um JSON listando todos os vendedores e suas vendas detalhadas
- `POST /api/comissoes` — Para adicionar vendedores caso queira

Exemplo:

```bash
curl http://localhost:8081/api/comissoes
```
