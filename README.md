# ⚡ Ranqueador de Usinas de Geração de energia

Aplicação desenvolvida em **Kotlin** com **Spring Boot** para ingestão de dados públicos da ANEEL (Geração de Energia) e exposição de métricas via API REST.

O projeto implementa uma arquitetura robusta focada em **Separação de Responsabilidades (SoC)**, alta performance na persistência de dados e resiliência a falhas de rede.

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Kotlin 1.9 (JVM 17/21)
* **Framework:** Spring Boot 3.4
* **Banco de Dados:** PostgreSQL 15 (via Docker)
* **ORM:** Spring Data JPA / Hibernate
* **Ferramentas:** Gradle, Apache Commons CSV, Docker Compose
* **Cliente HTTP:** Spring RestClient

## 🏗️ Arquitetura e Design Patterns

O projeto segue o padrão **MVC** com princípios de **Clean Architecture**, garantindo desacoplamento entre a camada de persistência e a camada de apresentação.

* **Controller:** Camada "burra", responsável apenas por orquestrar a entrada/saída HTTP.
* **DTO (Data Transfer Objects):** Garante que a entidade do banco de dados (`Entity`) nunca seja exposta diretamente na API, protegendo o contrato de dados.
* **Service Layer (CQRS Simplificado):**
    * `SyncService`: Responsável pela escrita (ETL, Download, Parse e Persistência).
    * `GeradorService`: Responsável pela leitura e regras de negócio de visualização.
* **Repository:** Abstração do acesso a dados via Spring Data JPA.

---

## ⚙️ Decisões Técnicas e Otimizações

Para garantir que a aplicação seja resiliente e performática, as seguintes estratégias foram adotadas:

### 1. Ingestão de Dados (Streaming vs Memória)
O arquivo CSV da ANEEL é processado via **InputStream** e lido linha a linha utilizando `Apache Commons CSV`. Isso evita carregar o arquivo inteiro na memória RAM (`OutOfMemoryError`), permitindo que a aplicação processe arquivos de Gigabytes com consumo de memória constante.

### 2. Performance de Banco de Dados (Batch Inserts)
A configuração padrão do Hibernate ("row-by-row") foi substituída por **JDBC Batching**.
* **Otimização:** Configurado `batch_size=50` e `order_inserts=true`.
* **Resultado:** Redução drástica no tempo de ingestão (processamento de ~1.000 registros/segundo), minimizando o *Network Overhead* entre a aplicação e o banco.


### 3. Tratamento de Encoding
Implementado o suporte forçado ao charset **ISO-8859-1** para garantir a correta acentuação dos nomes das usinas brasileiras (ex: *Portocém*, *Açu*), corrigindo problemas de compatibilidade com o padrão UTF-8.

---

## 🛠️ Como Rodar o Projeto

### Pré-requisitos
* Docker e Docker Compose instalados.
* JDK 17 ou superior.

### Passo 1: Subir a Infraestrutura
Na raiz do projeto, execute o comando para subir o banco de dados PostgreSQL:

```bash
docker-compose up -d
```
### Passo 2: Executar a Aplicação
Com o banco rodando, inicie a aplicação Spring Boot:

* **Linux/Mac:**
```bash
./gradlew bootRun
```

* **Windows:**
```bash
gradlew.bat bootRun
```

  **Nota:** Ao iniciar, a aplicação executará automaticamente o Job de importação (CsvImportJob). Aguarde a mensagem "Sincronização finalizada com sucesso!" no console.
**Em média está sendo salvo no banco 1000 usina por segundo. Como o arquivo de coleta contém cerca 420+mil registros inicialmente na primeira fez que rodar a aplicação levará cerca de 10 minutos para a sincronização está finalizada com sucesso, então é necessário aguardar para fazer a chamada na API. Após a primeira sincronização não é necessário aguardar 10 minutos visto que o Job de extração ocorrerá automático a cada 1 hora.**

### 🔌 Documentação da API
***Listar Top 5 Geradores***

Retorna as 5 maiores usinas em potência outorgada ordenadas de forma decrescente.

**Requisição:**
``` bash
GET /api/v1/geradores/top5
```
Resposta de Sucesso (200 OK):
``` bash

[
    {
    "nome": "Belo Monte",
    "codigo": "UHE.PH.PA.028863-0.01",
    "tipo": "UHE",
    "potencia": 11233.1
    },
    {
    "nome": "Tucuruí",
    "codigo": "UHE.PH.PA.027699-3.01",
    "tipo": "UHE",
    "potencia": 8535.0
    },
...
]
```

### 📅 Agendamento (Job)
O sistema possui um agendador (Scheduler) configurado para atualizar a base de dados periodicamente.

* **Frequência:** Configurada via @Scheduled (Padrão: 1 hora).

* **Estratégia:** Limpeza prévia (deleteAll) seguida de carga total otimizada em lotes.

### 👨‍💻 Autor

Desenvolvido por 
@devJhonL