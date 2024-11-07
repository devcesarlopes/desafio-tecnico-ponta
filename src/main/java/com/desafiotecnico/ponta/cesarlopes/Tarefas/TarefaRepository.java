package com.desafiotecnico.ponta.cesarlopes.Tarefas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TarefaRepository extends JpaRepository<Tarefa,Integer> {
    List<Tarefa> findByStatus(String status);
    List<Tarefa> findByCreatedById(UUID userId);
    Optional<Tarefa> findById(int id);
}