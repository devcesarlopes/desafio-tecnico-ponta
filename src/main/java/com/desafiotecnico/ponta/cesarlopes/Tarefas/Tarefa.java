package com.desafiotecnico.ponta.cesarlopes.Tarefas;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Tarefa {
    private String titulo;
    private String descricao;
    private Date dataDeCriacao;
    private String status;
}
