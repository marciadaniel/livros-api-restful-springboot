# API RESTful de Livros

Este projeto é uma API RESTful para gestão de livros, construído com Spring Boot e Java. Ele permite realizar operações CRUD (criar, ler, atualizar e excluir) em livros.

## Tecnologias Utilizadas

- **Java**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Validation** 
- **Spring HATEOAS**
- **PostgreSQL Database**
- **Spring Security**

## Como Executar
Acessa online em:
https://livros-api-restful-springboot.onrender.com

Ou também:

1. Clone este repositório.
2. Abra o projeto em sua IDE favorita (por exemplo, IntelliJ IDEA ou Eclipse).
3. Execute o arquivo `LivrosApplication.java` como um aplicativo Spring Boot.
4. Acesse a API em `http://localhost:8080`.

## Endpoints

- **GET /livros**: Retorna uma lista de livros.
- **POST /livros**: Cria um novo livro.
- **GET /livros/{id}**: Retorna um livro específico.
- **PUT /livros/{id}**: Atualiza um livro existente.
- **DELETE /livros/{id}**: Remove um livro existente.
