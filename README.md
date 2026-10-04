# sales-commission

API Spring Boot que registra as vendas do time comercial e calcula a comissão de cada vendedor.

As vendas ficam **em memória**: na inicialização a API carrega as vendas de `src/main/resources/vendas.json` e, a partir daí, novas vendas podem ser adicionadas via `POST /api/vendas`. O cálculo de comissão (`GET /api/comissoes`) sempre considera todas as vendas registradas (arquivo + as que foram adicionadas). Ao reiniciar a aplicação, volta-se ao conteúdo do arquivo.

## Regras

| Valor da venda | Comissão |
| --- | --- |
| Menor que R$ 100,00 | 0% |
| De R$ 100,00 até R$ 499,99 | 1% |
| A partir de R$ 500,00 | 5% |

- A comissão é calculada venda a venda e depois somada por vendedor.
- Os valores usam `BigDecimal` com duas casas decimais e arredondamento `HALF_UP`.
- Uma venda só é aceita se `vendedor` estiver preenchido e `valor` for maior que zero.

## Pré-requisitos

- Java 21 instalado (`JAVA_HOME` apontando para o JDK 21).
- Não é preciso instalar Maven: o Maven Wrapper (`mvnw.cmd`) baixa a versão correta automaticamente.

## Como executar

Execute esse comando no terminal, dentro da pasta do projeto:

```bash
.\mvnw.cmd spring-boot:run
```

A API sobe na porta **8081**.

Caso já tenha o java 21 instalado no seu computador, execute esse comando para trocar a versão e executar a aplicação

```bash
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
java -version
.\mvnw.cmd clean spring-boot:run
```

## Como rodar os testes

```bash
.\mvnw.cmd test
```

Os testes cobrem as faixas de comissão, o agrupamento por vendedor, o armazenamento em memória e o fluxo completo da API (adicionar venda e vê-la refletida na comissão).

## Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/vendas` | Lista todas as vendas registradas em memória |
| `POST` | `/api/vendas` | Adiciona vendas em memória (passam a entrar no cálculo de comissão) |
| `GET` | `/api/comissoes` | Comissão de todos os vendedores, considerando todas as vendas em memória |
| `GET` | `/api/comissoes/{vendedor}` | Comissão de um vendedor específico |
| `POST` | `/api/comissoes` | Simulação: calcula a comissão das vendas enviadas sem salvar nada |

Fluxo sugerido para testar no Postman:

1. `GET /api/comissoes` para ver o resultado com as vendas do arquivo.
2. `POST /api/vendas` com novas vendas.
3. `GET /api/comissoes` (ou `GET /api/comissoes/{vendedor}`) para ver as novas vendas refletidas no cálculo.

Todas as requisições com corpo usam o header `Content-Type: application/json`.

### GET /api/vendas

Retorna a lista de vendas registradas (arquivo + adicionadas).

Resposta `200 OK`:

```json
[
  { "vendedor": "João Silva", "valor": 1200.50 },
  { "vendedor": "João Silva", "valor": 950.75 },
  { "vendedor": "Maria Souza", "valor": 2100.40 }
]
```

### POST /api/vendas

Adiciona uma ou mais vendas ao armazenamento em memória.

Request:

```json
{
  "vendas": [
    { "vendedor": "Pedro Santos", "valor": 750.00 },
    { "vendedor": "Pedro Santos", "valor": 80.00 }
  ]
}
```

Resposta `201 Created` (apenas as vendas que foram adicionadas nesta requisição):

```json
[
  { "vendedor": "Pedro Santos", "valor": 750.00 },
  { "vendedor": "Pedro Santos", "valor": 80.00 }
]
```

Resposta `400 Bad Request` quando algum campo é inválido (vendedor vazio, valor ausente ou menor/igual a zero, lista `vendas` vazia):

```json
{
  "erro": "Dados inválidos",
  "detalhes": [
    "vendas[0].vendedor: vendedor é obrigatório",
    "vendas[0].valor: valor deve ser maior que zero"
  ]
}
```

### GET /api/comissoes

Calcula a comissão de todos os vendedores sobre todas as vendas em memória.

Resposta `200 OK` (um item por vendedor; exemplo resumido):

```json
[
  {
    "vendedor": "Pedro Santos",
    "quantidadeVendas": 2,
    "totalVendas": 830.00,
    "totalComissao": 37.50,
    "detalhes": [
      { "valorVenda": 750.00, "percentual": 0.05, "comissao": 37.50 },
      { "valorVenda": 80.00, "percentual": 0, "comissao": 0.00 }
    ]
  }
]
```

### GET /api/comissoes/{vendedor}

Mesmo cálculo, mas apenas para o vendedor informado (a busca não diferencia maiúsculas de minúsculas). Exemplo: `GET /api/comissoes/Ana Lima`.

Resposta `200 OK`:

```json
{
  "vendedor": "Ana Lima",
  "quantidadeVendas": 9,
  "totalVendas": 8763.95,
  "totalComissao": 404.99,
  "detalhes": [
    { "valorVenda": 1000.00, "percentual": 0.05, "comissao": 50.00 },
    { "valorVenda": 1100.50, "percentual": 0.05, "comissao": 55.03 },
    { "valorVenda": 75.30, "percentual": 0, "comissao": 0.00 },
    { "valorVenda": 420.90, "percentual": 0.01, "comissao": 4.21 }
  ]
}
```

Resposta `404 Not Found` se o vendedor não tiver vendas registradas:

```json
{ "erro": "Vendedor não encontrado: Zeca" }
```

### POST /api/comissoes (simulação)

Calcula a comissão apenas das vendas enviadas no corpo. **Nada é salvo**: as vendas enviadas aqui não aparecem em `GET /api/vendas` nem em `GET /api/comissoes`. Útil para testar as faixas de comissão rapidamente. Para adicionar vendas de fato, use `POST /api/vendas`.

Request:

```json
{
  "vendas": [
    { "vendedor": "Teste", "valor": 250.30 },
    { "vendedor": "Teste", "valor": 90.00 }
  ]
}
```

Resposta `200 OK`:

```json
[
  {
    "vendedor": "Teste",
    "quantidadeVendas": 2,
    "totalVendas": 340.30,
    "totalComissao": 2.50,
    "detalhes": [
      { "valorVenda": 250.30, "percentual": 0.01, "comissao": 2.50 },
      { "valorVenda": 90.00, "percentual": 0, "comissao": 0.00 }
    ]
  }
]
```

### Exemplo com curl

```bash
curl http://localhost:8081/api/comissoes

curl -X POST http://localhost:8081/api/vendas ^
  -H "Content-Type: application/json" ^
  -d "{\"vendas\":[{\"vendedor\":\"Pedro Santos\",\"valor\":750.00}]}"
```
