package ru.dterenteva.api.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dterenteva.api.entities.User;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    public Optional<User> findByName(String name);
    public Boolean existsByName(String name);
    public Optional<User> findByOwnerId(int id);
}
