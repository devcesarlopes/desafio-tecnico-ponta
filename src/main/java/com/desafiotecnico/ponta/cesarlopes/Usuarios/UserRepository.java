package com.desafiotecnico.ponta.cesarlopes.Usuarios;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndPassword(String login, String password);
}