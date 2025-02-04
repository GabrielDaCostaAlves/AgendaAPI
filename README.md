# 🚀 API de Gerenciamento de Contatos

### 📌 Descrição
Esta **API RESTful** foi construída utilizando **Java 23** e **Spring Boot 3**, implementando os princípios da **Clean Architecture** para garantir um sistema modular, fácil de testar e escalável. O foco principal da aplicação é o gerenciamento de **usuários**, **contatos**, **telefones** e **endereços**, com segurança e controle de acesso baseados em **JWT**. A arquitetura foi projetada para ser robusta, permitindo que a aplicação cresça de forma organizada.

## 🛠 Tecnologias Utilizadas

- **Java 23** + **Spring Boot 3**: Plataforma moderna e poderosa para construir aplicações robustas.
- **Spring Security + JWT**: Protege os endpoints com autenticação baseada em token e autorização com roles.
- **MySQL + Liquibase**: Persistência de dados utilizando MySQL, com controle de versão do banco de dados através do Liquibase.
- **JUnit 5 + Mockito + H2**: Testes automatizados com cobertura de testes unitários e de integração, garantindo a confiabilidade do sistema.
- **Springdoc OpenAPI**: Geração de documentação interativa da API para facilitar a integração com clientes.
- **HATEOAS**: Implementação de links dinâmicos para navegar entre os recursos da API de forma intuitiva.

## 📂 Estrutura do Projeto

```bash
src/
├── main/
│   ├── java/com/agendaapi/
│   │   ├── assembler/        # Transformação de entidades para VOs (HATEOAS)
│   │   ├── config/           # Configurações gerais da aplicação
│   │   ├── controller/       # Controladores REST
│   │   ├── domain/           # Camada de domínio
│   │   │   ├── model/        # Entidades do banco de dados
│   │   │   ├── repository/   # Interfaces para acesso ao banco
│   │   │   ├── service/      # Regras de negócio
│   │   ├── dto/              # Data Transfer Objects (DTOs)
│   │   ├── exception/        # Tratamento de exceções
│   │   ├── security/         # Implementação de autenticação
│   │   ├── util/             
│   │   │   ├── conversor/    # Conversores personalizados (substituindo MapStruct)
│   │   ├── vo/               # Objetos de valor (HATEOAS)
│   ├── resources/
│   │   ├── db/changelog/     # Scripts Liquibase
│   │   ├── application.properties  # Configuração da aplicação
├── test/
│   ├── java/com/agendaapi/
│   │   ├── controller/       # Testes de Controllers
│   │   ├── service/          # Testes de Serviços
│   │   ├── repository/       # Testes de Repositórios
│   │   ├── security/         # Testes de Segurança
```

## 🔑 Autenticação e Autorização

A API utiliza **JWT (JSON Web Token)** para autenticação, garantindo que apenas usuários autenticados possam acessar os recursos. O controle de acesso é feito com base em **roles** atribuídas aos usuários:

- **ROLE_ADMINISTRATOR**: Acesso total (CRUD de usuários e contatos).
- **ROLE_CUSTOMER**: Acesso restrito aos dados do próprio usuário e seus contatos.

### 🔐 Obtenção do Token JWT
Para obter o token JWT, faça uma requisição **POST** para o endpoint `/login` com as credenciais do usuário:

```json
{
  "email": "usuario@email.com",
  "password": "senha123"
}
```

A resposta incluirá o token JWT, que deve ser incluído nas requisições subsequentes, no header `Authorization`:

```bash
Authorization: Bearer <seu_token>
```

## 📜 Documentação da API

A documentação interativa da API está disponível no **Swagger UI**, gerada automaticamente pelo **Springdoc OpenAPI**. Acesse:

```
http://localhost:8080/swagger-ui.html
```

Esta interface facilita a exploração dos endpoints da API e oferece informações detalhadas sobre cada recurso disponível, incluindo exemplos de requisições e respostas.

## 📦 Instalação e Execução

### 1️⃣ Clonar o repositório
```bash
git clone https://github.com/seu-usuario/agenda-api.git
cd agenda-api
```

### 2️⃣ Configurar variáveis de ambiente
Edite o arquivo `application.properties` para configurar a conexão com o banco de dados:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agenda_api
spring.datasource.username=root
spring.datasource.password=root
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.xml
```

### 3️⃣ Construir e executar a aplicação
Para compilar e rodar a aplicação, execute:

```bash
./mvnw clean install
java -jar target/agenda-api.jar
```

## ✅ Testes

A aplicação vem com **testes automatizados** para garantir a qualidade do código e a funcionalidade das principais features. Os testes podem ser executados com:

```bash
./mvnw test
```

Os testes cobrem as principais operações CRUD e a segurança da API, utilizando **JUnit 5** para testes unitários e **Mockito** para mocks de dependências. A execução de testes é feita em um banco **H2** em memória, facilitando a integração contínua e o desenvolvimento ágil.

## 📌 Contribuição

Contribuições são bem-vindas! Para sugerir melhorias ou corrigir bugs, por favor:

1. Crie uma **issue** para discutir a mudança.
2. Faça um **fork** do repositório.
3. Abra um **Pull Request** com suas mudanças.