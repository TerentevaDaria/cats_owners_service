package ru.dterenteva.api.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dterenteva.api.entities.Role;

public interface RoleDao extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
