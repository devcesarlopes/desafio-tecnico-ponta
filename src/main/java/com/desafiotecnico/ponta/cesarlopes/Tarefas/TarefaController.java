package com.desafiotecnico.ponta.cesarlopes.Tarefas;

import com.desafiotecnico.ponta.cesarlopes.Exceptions.CustomException;
import com.desafiotecnico.ponta.cesarlopes.Token.Token;
import com.desafiotecnico.ponta.cesarlopes.Usuarios.User;
import com.desafiotecnico.ponta.cesarlopes.Usuarios.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public List<Tarefa> getAllTarefas(HttpServletRequest request) {
        Token.obterIdDeUsuarioAtravesDoToken(request);
        return tarefaRepo.findAll();
    }

    @PostMapping
    public Tarefa createTarefa(@RequestBody @Valid Tarefa tarefa, HttpServletRequest request) throws CustomException {
        UUID userId = Token.obterIdDeUsuarioAtravesDoToken(request);

        if (!Status.statusEhValido(tarefa.getStatus())) {
            throw new CustomException(String.format("O atributo \"status\" deve ter valor: \"%s\", \"%s\" ou \"%s\"",
                    Status.PENDENTE, Status.EM_ANDAMENTO, Status.CONCLUIDA));
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new CustomException("Usuario nao encontrado."));
        tarefa.setCreatedBy(user);
        Tarefa dbTarefa = tarefaRepo.save(tarefa);

        User _user = new User();
        _user.setId(dbTarefa.getCreatedBy().getId());
        _user.setName(dbTarefa.getCreatedBy().getName());
        dbTarefa.setCreatedBy(_user);

        return dbTarefa;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTarefa(@PathVariable int id, @RequestBody Tarefa tarefaDetails, HttpServletRequest request) throws CustomException {
        UUID userId = Token.obterIdDeUsuarioAtravesDoToken(request);

        Tarefa tarefa = tarefaRepo.findById(id)
                .orElseThrow(() -> new CustomException("Tarefa nao encontrada"));

        if (!tarefa.getCreatedBy().getId().equals(userId)) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        if (tarefaDetails.getTitulo() != null) {
            tarefa.setTitulo(tarefaDetails.getTitulo());
        }
        if (tarefaDetails.getDescricao() != null) {
            tarefa.setDescricao(tarefaDetails.getDescricao());
        }
        if (tarefaDetails.getStatus() != null) {
            if (!Status.statusEhValido(tarefa.getStatus())) {
                throw new CustomException(String.format("O atributo \"status\" deve ter valor: \"%s\", \"%s\" ou \"%s\"",
                        Status.PENDENTE, Status.EM_ANDAMENTO, Status.CONCLUIDA));
            }
            tarefa.setStatus(tarefaDetails.getStatus());
        }

        Tarefa updatedTarefa = tarefaRepo.save(tarefa);
        return ResponseEntity.ok(updatedTarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTarefa(@PathVariable int id, HttpServletRequest request) throws CustomException {
        UUID userId = Token.obterIdDeUsuarioAtravesDoToken(request);

        Tarefa tarefa = tarefaRepo.findById(id)
                .orElseThrow(() -> new CustomException("Tarefa nao encontrada"));

        if (!tarefa.getCreatedBy().getId().equals(userId)) {
            return ResponseEntity.status(403).body("Usuario nao autorizado");
        }

        tarefaRepo.delete(tarefa);
        return ResponseEntity.ok("Tarefa removida com sucesso");
    }
}
