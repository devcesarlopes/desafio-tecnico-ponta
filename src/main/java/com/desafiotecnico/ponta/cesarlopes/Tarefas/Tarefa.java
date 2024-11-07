package com.desafiotecnico.ponta.cesarlopes.Tarefas;

import com.desafiotecnico.ponta.cesarlopes.Usuarios.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "tarefas")
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O atributo \"titulo\" e obrigatorio.")
    private String titulo;

    @NotBlank(message = "O atributo \"descricao\" e obrigatorio.")
    private String descricao;

    @NotNull(message = "O atributo \"dataDeCriacao\" e obrigatorio.")
    private Date dataDeCriacao;

    @NotBlank(message = "O atributo \"status\" e obrigatorio.")
    private String status;


    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        created = new Date();
        updated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
