package com.desafiotecnico.ponta.cesarlopes.Usuarios;

import com.desafiotecnico.ponta.cesarlopes.Exceptions.CustomException;
import com.desafiotecnico.ponta.cesarlopes.Token.LoginRequest;
import com.desafiotecnico.ponta.cesarlopes.Token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/usuarios")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) throws CustomException {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new CustomException(String.format("User with id %s not found", id)));
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) throws CustomException {
        System.out.println("Creating a new user");

        if (userRepo.findByLogin(user.getLogin()).isPresent()) {
            throw new CustomException(String.format("Login %s is already taken.", user.getLogin()));
        }

        return userRepo.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) throws CustomException {
        User dbUser = userRepo.findById(id)
                .orElseThrow(() -> new CustomException(String.format("User with id %s not found", id)));

        if (user.getName() != null) {
            dbUser.setName(user.getName());
        }
        if (user.getLogin() != null) {
            dbUser.setLogin(user.getLogin());
        }
        if (user.getPassword() != null) {
            // Automatically hashes password
            dbUser.setPassword(user.getPassword());
        }

        User updatedUser = userRepo.save(dbUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) throws CustomException {
        User dbUser = userRepo.findById(id)
                .orElseThrow(() -> new CustomException(String.format("User with id %s not found", id)));

        userRepo.delete(dbUser);
        return ResponseEntity.ok(String.format("User with id %s was successfully deleted", id));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws CustomException {

        Optional<User> user = userRepo.findByLoginAndPassword(loginRequest.getLogin(), loginRequest.getPassword());

        if (user.isPresent()) {
            String token = Token.gerarToken(user.get());
            return ResponseEntity.ok().header("Authorization", "Bearer " + token)
                    .body("{\"token\": \"Bearer " + token + "\"}");
        } else {
            return ResponseEntity.status(401).body("Invalid login or password");
        }
    }
}
