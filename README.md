# sales-commission

Aplicação que lê as vendas do time comercial e calcula a comissão de cada vendedor.

Ao iniciar, a aplicação já carrega as vendas do arquivo `src/main/resources/vendas.json`. Você também pode adicionar novas vendas pela API, e elas passam a entrar no cálculo da comissão. Os dados ficam só em memória: ao reiniciar, volta ao conteúdo do arquivo.

## Regras de comissão

| Valor da venda | Comissão |
| --- | --- |
| Menor que R$ 100,00 | 0% |
| De R$ 100,00 até R$ 499,99 | 1% |
| A partir de R$ 500,00 | 5% |

A comissão é calculada venda a venda e depois somada por vendedor. Os valores são mostrados com duas casas decimais.

## Pré-requisitos

- Java 21 instalado.
- Não precisa instalar Maven: o projeto já vem com tudo que precisa para rodar.

## Como executar

Abra o terminal na pasta do projeto e rode:

```bash
.\mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8081`.

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

## Como testar (Postman ou Insomnia)

Nas chamadas `POST`, selecione o corpo como **JSON** (raw).

Sugestão de ordem:

1. **GET** `http://localhost:8081/api/comissoes` para ver a comissão com as vendas do arquivo.
2. **POST** `http://localhost:8081/api/vendas` para adicionar novas vendas.
3. **GET** `http://localhost:8081/api/comissoes` de novo para ver as novas vendas no resultado.

### Listar vendas

**GET** `http://localhost:8081/api/vendas`

Resposta:

```json
[
  { "vendedor": "João Silva", "valor": 1200.50 },
  { "vendedor": "João Silva", "valor": 950.75 },
  { "vendedor": "Maria Souza", "valor": 2100.40 }
]
```

### Adicionar vendas

**POST** `http://localhost:8081/api/vendas`

Corpo:

```json
{
  "vendas": [
    { "vendedor": "Pedro Santos", "valor": 750.00 },
    { "vendedor": "Pedro Santos", "valor": 80.00 }
  ]
}
```

Resposta (201 Created) com as vendas que foram adicionadas:

```json
[
  { "vendedor": "Pedro Santos", "valor": 750.00 },
  { "vendedor": "Pedro Santos", "valor": 80.00 }
]
```

Se algum campo estiver errado (vendedor vazio, valor zerado ou negativo, lista vazia), a resposta é 400 Bad Request:

```json
{
  "erro": "Dados inválidos",
  "detalhes": [
    "vendas[0].vendedor: vendedor é obrigatório",
    "vendas[0].valor: valor deve ser maior que zero"
  ]
}
```

### Comissão de todos os vendedores

**GET** `http://localhost:8081/api/comissoes`

Resposta (um item por vendedor):

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

### Comissão de um vendedor

**GET** `http://localhost:8081/api/comissoes/Ana Lima`

Não diferencia maiúsculas de minúsculas.

Resposta:

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

Se o vendedor não existir, a resposta é 404 Not Found:

```json
{ "erro": "Vendedor não encontrado: Zeca" }
```

### Simular comissão sem salvar

**POST** `http://localhost:8081/api/comissoes`

Calcula a comissão só das vendas enviadas, sem guardar nada. Serve para testar as faixas rapidamente. Para adicionar vendas de verdade, use **POST** `http://localhost:8081/api/vendas`.

Corpo:

```json
{
  "vendas": [
    { "vendedor": "Teste", "valor": 250.30 },
    { "vendedor": "Teste", "valor": 90.00 }
  ]
}
```

Resposta:

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
