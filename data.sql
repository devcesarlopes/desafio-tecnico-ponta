CREATE TABLE users (uid SERIAL, name VARCHAR(255), size INTEGER, password VARCHAR(255), created DATE, updated DATE)
CREATE TABLE tarefas ( id SERIAL PRIMARY KEY, titulo VARCHAR(255) NOT NULL, descricao TEXT, data_de_criacao DATE, status VARCHAR(50), created DATE, updated DATE);
