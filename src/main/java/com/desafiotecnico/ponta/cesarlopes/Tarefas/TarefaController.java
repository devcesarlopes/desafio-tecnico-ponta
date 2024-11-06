package com.desafiotecnico.ponta.cesarlopes.Tarefas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/tarefas")
public class TarefaController {
    private List<Tarefa> tarefas = Arrays.asList(
            new Tarefa("titulo1", "descricao1", new Date(), "pendente"),
            new Tarefa("titulo2", "descricao2", new Date(), "concluída")
    );

    @GetMapping
    public List<Tarefa> findAllTarefas() {
        return tarefas;
    }
}
