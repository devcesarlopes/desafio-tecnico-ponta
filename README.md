# Desafio Técnico PONTA - API de Gestão de Tarefas e Usuários

Este projeto é um desafio técnico para uma posição na **PONTA**. Trata-se de uma API RESTful criada para gerenciar usuários e tarefas, oferecendo endpoints para operações CRUD (Criar, Ler, Atualizar, Excluir) em ambos. A API é projetada com autenticação, validação e tratamento de erros.

## Visão Geral do Projeto

A API oferece funcionalidades para:
- **Gestão de Usuários**: Criar, atualizar, consultar e excluir usuários.
- **Gestão de Tarefas**: Criar, atualizar, consultar e excluir tarefas associadas aos usuários.
- **Autenticação**: Autenticação com tokens JWT (JSON Web Tokens) para proteger o acesso aos endpoints.

### Tecnologias Utilizadas
- **Spring Boot**: Framework para desenvolvimento da API.
- **JWT (JSON Web Tokens)**: Autenticação e autorização.
- **Hibernate/JPA**: Mapeamento objeto-relacional e interação com o banco de dados.
- **PostgreSQL**: Banco de dados para armazenamento de usuários e tarefas.
- **H2 (para testes)**: Banco de dados em memória para testes unitários.
- **JUnit e MockMVC**: Frameworks para testes de integração e unitários.

## Documentação e URL da API

A documentação completa da API está disponível no SwaggerHub e a API pode ser acessada no servidor abaixo:
- **Swagger**: [Documentação Swagger](https://app.swaggerhub.com/apis-docs/DEVCESARLOPES/desafio_tecnico_ponta_cesar_lopes/1.0.0)
- **URL do Servidor**: [https://desafio-tecnico-ponta.onrender.com/api/v1/](https://desafio-tecnico-ponta.onrender.com/api/v1/)

## Endpoints da API

A API fornece os seguintes endpoints:

### Endpoints de Usuário

- **Criar Usuário**: `POST /api/v1/usuarios`
  - Corpo da Requisição: `{ "name": "string", "login": "string", "password": "string" }`
- **Login**: `POST /api/v1/usuarios/login`
  - Corpo da Requisição: `{ "login": "string", "password": "string" }`
  - Resposta: token JWT para autenticação.
- **Consultar Todos os Usuários**: `GET /api/v1/usuarios`
- **Consultar Usuário por ID**: `GET /api/v1/usuarios/{id}`
- **Atualizar Usuário**: `PUT /api/v1/usuarios/{id}`
- **Excluir Usuário**: `DELETE /api/v1/usuarios/{id}`

### Endpoints de Tarefa

- **Criar Tarefa**: `POST /api/v1/tarefas`
  - Necessita de token JWT no cabeçalho `Authorization`.
  - Corpo da Requisição: `{ "titulo": "string", "descricao": "string", "dataDeCriacao": "date", "status": "string" }`
- **Consultar Todas as Tarefas**: `GET /api/v1/tarefas`
- **Consultar Tarefa por ID**: `GET /api/v1/tarefas/{id}`
- **Atualizar Tarefa**: `PUT /api/v1/tarefas/{id}`
- **Excluir Tarefa**: `DELETE /api/v1/tarefas/{id}`

### Autenticação

Os endpoints de tarefas requerem o cabeçalho `Authorization` com um token JWT.

### Exemplos de Requisições

Aqui estão alguns exemplos de requisições `cURL` para interagir com a API:

#### Criar um Novo Usuário
```bash
curl -X POST "https://desafio-tecnico-ponta.onrender.com/api/v1/usuarios" -H "Content-Type: application/json" -d '{
    "name": "John Doe",
    "login": "johndoe",
    "password": "password123"
}'
```

#### Login e Obtenção do Token JWT
```bash
curl -X POST "https://desafio-tecnico-ponta.onrender.com/api/v1/usuarios/login" -H "Content-Type: application/json" -d '{
    "login": "johndoe",
    "password": "password123"
}'
```
#### Criar uma Tarefa
```bash
curl -X POST "https://desafio-tecnico-ponta.onrender.com/api/v1/tarefas" -H "Authorization: Bearer <seu_token>" -H "Content-Type: application/json" -d '{
    "titulo": "Nova Tarefa",
    "descricao": "Esta é uma tarefa de exemplo",
    "dataDeCriacao": "2024-11-07",
    "status": "pendente"
}'
```

## Tratamento de Erros

- **400 Bad Request**: Erros de validação, como campos obrigatórios ausentes ou valores de status inválidos.
- **401 Unauthorized**: Token JWT inválido ou ausente em rotas protegidas.
- **404 Not Found**: Quando um ID de usuário ou tarefa solicitado não existe.
- **500 Internal Server Error**: Erros de servidor não tratados.

## Observações

- **Integridade de Dados**: Ao excluir um usuário, todas as tarefas associadas a ele são excluídas automaticamente.
- **Validação**: As tarefas requerem os campos `titulo`, `descricao`, `dataDeCriacao` e um `status` válido.

## Licença

Este projeto faz parte de um desafio técnico para a PONTA e é destinado apenas para fins de avaliação e aprendizado.

