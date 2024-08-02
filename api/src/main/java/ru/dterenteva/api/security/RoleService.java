package ru.dterenteva.api.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dterenteva.api.entities.CustomGrantedAuthority;
import ru.dterenteva.api.entities.Role;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class RoleService {
    private RoleDao roleDao;

    public int addRole(CustomGrantedAuthority role) {
        Role roleEntity = new Role();
        roleEntity.setName(role.getName());

        roleDao.save(roleEntity);
        return roleEntity.getId();
    }
}
