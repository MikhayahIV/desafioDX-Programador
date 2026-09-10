# Desafio Duxus — Sistema de Escalação de Times

API REST para cadastro de integrantes e times, além do processamento de
estatísticas relacionadas às escalações.

O projeto permite consultar informações como integrante mais utilizado,
clube mais recorrente, função mais recorrente e contagens por período.

---

## Aplicação online

O projeto está disponível online para demonstração:

**Frontend / Dashboard:**
[https://team-analytics-dash.vercel.app/times](https://team-analytics-dash.vercel.app/)

A aplicação possui uma arquitetura separada entre frontend, backend e
banco de dados.

---

## Arquitetura e Deploy

A aplicação foi publicada utilizando serviços separados para cada camada:

```text
                    ┌─────────────────────┐
                    │      Frontend       │
                    │       Vercel        │
                    └──────────┬──────────┘
                               │
                               │ HTTP / REST
                               ▼
                    ┌─────────────────────┐
                    │       Backend       │
                    │       Render        │
                    │   Spring Boot API   │
                    └──────────┬──────────┘
                               │
                               │ PostgreSQL
                               ▼
                    ┌─────────────────────┐
                    │      Database       │
                    │       Render        │
                    │     PostgreSQL      │
                    └─────────────────────┘
```

### Frontend

O frontend foi desenvolvido com auxílio de inteligência artificial através
do Lovable, sendo posteriormente integrado à API REST desenvolvida no
backend.

O frontend foi publicado na Vercel e configurado para consumir a API
hospedada no Render por meio de variável de ambiente.

### Backend

O backend foi desenvolvido em Java com Spring Boot e disponibilizado
na Render como um Web Service independente.

As configurações de conexão com o banco de dados são realizadas através de
variáveis de ambiente, evitando que informações de infraestrutura sejam
mantidas diretamente no código-fonte.

### Banco de dados

O banco utilizado é PostgreSQL, também hospedado na Render como um
serviço separado do backend.

A aplicação backend utiliza as variáveis de ambiente fornecidas pelo
ambiente de deploy para estabelecer a conexão com o banco.

### Variáveis de ambiente

As variáveis de ambiente foram configuradas separadamente nos ambientes de
desenvolvimento e produção.

No frontend, a variável de ambiente aponta para a URL da API publicada.

No backend, as variáveis são utilizadas para configurar a conexão com o
PostgreSQL.

### Configuração local

Para executar o backend localmente, utilize Java 11, Maven e PostgreSQL.

Configure as variáveis de ambiente utilizadas pela aplicação:

```
DB_NAME=NOME_DATABASE
DB_PASSWORD=SUA_SENHA_AQUI
DB_USER=SEU_USUARIO
DB_PORT=5432
DB_DRIVER=org.postgresql.Driver
DB_URL=URL_DO_DATABASE
```

**Importante:** o arquivo `.env` utilizado localmente não deve ser
versionado no Git. Utilize valores próprios para o ambiente local e nunca
publique credenciais reais no repositório.

Durante o desenvolvimento, o PostgreSQL local foi executado via Docker,
utilizando as mesmas variáveis definidas no `.env` acima (nome do banco,
usuário, senha e porta). A visualização e consulta direta do banco foi
feita com o DBeaver, conectando com essas mesmas credenciais.

## Como rodar localmente

### Pré-requisitos

- Java 11
- Maven
- PostgreSQL
- Git

Após configurar o banco de dados e as variáveis de ambiente, execute:

```bash
mvn spring-boot:run
```

A API será disponibilizada em:

```
http://localhost:8080
```

## Endpoints

### Cadastro

**Cadastrar integrante**
```
POST /integrantes
Content-Type: application/json
```

Exemplo:
```json
{
  "nome": "Michael Jordan",
  "funcao": "ala"
}
```

**Cadastrar time**

A composição deve referenciar integrantes já cadastrados pelo `id`.

```
POST /times
Content-Type: application/json
```

Exemplo:
```json
{
  "nomeDoClube": "Chicago Bulls",
  "data": "1995-01-01",
  "composicaoTime": [
    { "integrante": { "id": 1 } },
    { "integrante": { "id": 2 } },
    { "integrante": { "id": 3 } }
  ]
}
```

### Consultas e processamento

| Endpoint | Parâmetros | Descrição |
|---|---|---|
| `GET /times` | `data` (obrigatório) | Retorna o primeiro time encontrado para a data informada |
| `GET /times/mais-recorrente/integrantes` | `dataInicial`, `dataFinal` (opcionais) | Retorna os integrantes do time mais recorrente no período |
| `GET /integrantes/mais-usado` | `dataInicial`, `dataFinal` (opcionais) | Retorna o integrante presente no maior número de times no período |
| `GET /integrantes/funcao/mais-recorrente` | `dataInicial`, `dataFinal` (opcionais) | Retorna a função mais recorrente no período |
| `GET /integrantes/funcao/contagem` | `dataInicial`, `dataFinal` (opcionais) | Retorna a contagem de aparições por função |
| `GET /clubes/mais-recorrente` | `dataInicial`, `dataFinal` (opcionais) | Retorna o clube mais recorrente no período |
| `GET /clubes/contagem` | `dataInicial`, `dataFinal` (opcionais) | Retorna a contagem de aparições por clube |

As datas devem utilizar o formato ISO:

```
yyyy-MM-dd
```

Os parâmetros `dataInicial` e `dataFinal` podem ser omitidos para considerar
todo o histórico disponível.

Também é possível informar somente uma das datas para estabelecer apenas
um limite do período.

### Exemplos de requisições

```
GET /times?data=1995-01-01
GET /integrantes/mais-usado?dataInicial=1993-01-01&dataFinal=1995-01-01
GET /times/mais-recorrente/integrantes?dataInicial=1993-01-01&dataFinal=1995-01-01
GET /integrantes/funcao/mais-recorrente?dataInicial=1993-01-01&dataFinal=1995-01-01
GET /integrantes/funcao/contagem?dataInicial=1993-01-01&dataFinal=1995-01-01
GET /clubes/mais-recorrente?dataInicial=1993-01-01&dataFinal=1995-01-01
GET /clubes/contagem?dataInicial=1993-01-01&dataFinal=1995-01-01
```

Também é possível consultar sem informar o período:

```
GET /clubes/contagem
```

## Decisões de implementação

### Entidade Time

O endpoint `GET /times` retorna a entidade `Time` completa, incluindo sua
estrutura de `composicaoTime` e os respectivos `integrante`.

A implementação utiliza diretamente as entidades, sem a criação de DTO
específico para esse endpoint.

Quando existem múltiplos times cadastrados para a mesma data, o endpoint
retorna o primeiro registro encontrado.

### Time mais recorrente

Para a consulta de integrantes do time mais recorrente, um Time é
considerado pela combinação:

```
clube + composição
```

Dessa forma, duas escalações do mesmo clube somente são consideradas o
mesmo time quando possuem a mesma composição.

A ordem dos integrantes na composição não altera essa identificação.

### Integrante mais utilizado

O integrante é contabilizado pelo número de times dos quais participou no
período, evitando contar múltiplas vezes o mesmo integrante dentro de uma
mesma escalação.

### Filtro de período

O método `estaNoPeriodo` permite utilizar:

- `dataInicial` e `dataFinal`;
- somente `dataInicial`;
- somente `dataFinal`;
- nenhuma das duas datas.

Quando uma das datas não é informada, aquele limite do intervalo é
desconsiderado.

### Empates

O desafio não define um critério específico para situações de empate.

A implementação mantém o primeiro resultado encontrado durante o
processamento como critério de desempate.

### Tratamento de exceções

Não foi implementado um tratamento global/customizado de exceções.

Situações como:

- consulta de um time inexistente;
- cadastro de um time referenciando um integrante inexistente;

utilizam atualmente o tratamento padrão do Spring.

Um tratamento global com respostas HTTP mais específicas e padronizadas pode
ser implementado futuramente.

## Testes

Foram implementados testes unitários para os métodos de processamento do
`ApiService`.

Os testes cobrem:

- busca de time por data;
- integrante mais utilizado;
- integrantes do time mais recorrente;
- função mais recorrente;
- clube mais recorrente;
- contagem de clubes;
- contagem por função.

Para executar os testes:

```bash
mvn test
```

## Tecnologias

### Backend
- Java 11
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- JUnit

### Frontend
- React
- Lovable
- Vercel

### Infraestrutura
- Render
- PostgreSQL
- Variáveis de ambiente
- API REST

## Estrutura geral

```
src/
├── main/
│   └── java/
│       └── br/com/duxusdesafio/
│           ├── controller/
│           ├── entity/
│           ├── repository/
│           ├── service/
│           └── exception/
│
└── test/
    └── java/
        └── br/com/duxusdesafio/
```

O processamento das regras de negócio é centralizado no `ApiService`,
enquanto os controllers são responsáveis pela exposição dos endpoints REST.

## Observações

O projeto prioriza a implementação das regras de processamento solicitadas
no desafio, mantendo a lógica de negócio centralizada no `ApiService`.

A implementação atual utiliza as entidades JPA diretamente nos endpoints,
sem DTOs, como uma decisão de simplificação para a primeira versão.

Algumas melhorias de arquitetura e tratamento de erros estão documentadas
na seção de melhorias futuras.

## Melhorias futuras

As ideias abaixo não foram implementadas neste desafio — ficam registradas
como próximos passos.

### DTOs de request e response

Hoje os controllers trafegam as entidades JPA diretamente. Isso funciona,
mas acopla o contrato da API ao modelo de persistência (qualquer mudança de
coluna afeta o JSON exposto) e faz com que o GET /times não utilize o formato de resposta sugerido no enunciado. Uma próxima etapa seria:

- `IntegranteRequestDTO` / `IntegranteResponseDTO` — separa o que é
  aceito no cadastro do que é exposto nas consultas.
- `TimeRequestDTO` (recebe `nomeDoClube`, `data` e uma lista de
  `integranteId`, sem expor a estrutura de `ComposicaoTime`) e
  `TimeResponseDTO` (`data`, `clube`, `integrantes: [nomes]`), alinhando o
  `GET /times` ao formato do enunciado.
- Uma camada de *mapper* (manual ou com MapStruct) entre entidade e DTO,
  para não misturar essa conversão dentro do `ApiService`.

### Tratamento global de exceções

Um `@ControllerAdvice` com `@ExceptionHandler` para `NotFoundException` e
outras exceções de negócio, padronizando os códigos HTTP retornados (ex:
404 para time/integrante não encontrado) em vez do comportamento padrão do
Spring.

### Documentação Swagger/OpenAPI

Expor os endpoints via springdoc-openapi, facilitando a exploração e o
teste da API sem depender apenas deste README.

### Melhorias de testes

Ampliar a cobertura de testes unitários e de integração, incluindo casos de
borda como empates, múltiplos times na mesma data e períodos parciais
(somente `dataInicial` ou somente `dataFinal`).

### Validações

Validação de entrada nos endpoints de cadastro (ex: `@Valid` com
`@NotBlank`/`@NotNull` em `Integrante` e `Time`), evitando cadastros com
dados incompletos ou inconsistentes.
