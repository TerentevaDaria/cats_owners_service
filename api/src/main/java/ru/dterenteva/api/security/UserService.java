package ru.dterenteva.api.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dterenteva.api.dto.RegistrationRequest;
import ru.dterenteva.api.entities.CustomUserDetails;
import ru.dterenteva.api.entities.User;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return getUserByName(username);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public int addUser(RegistrationRequest request) {
        if (userDao.existsByName(request.getName())) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = mapper.RegistrationRequestToUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user.setPassword(user.getPassword());

        userDao.save(user);

        return user.getId();
    }

    public CustomUserDetails getUserById(int id) {
        return mapper.userToUserDetails(userDao.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    public CustomUserDetails getUserByOwnerId(int id) {
        return mapper.userToUserDetails(userDao.findByOwnerId(id).orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    public CustomUserDetails getUserByName(String name) {
        return mapper.userToUserDetails(userDao.findByName(name).orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    public void deleteUserById(int id) {
        if (!userDao.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }

        userDao.deleteById(id);
    }
}
