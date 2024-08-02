package ru.dterenteva.api.security;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.dterenteva.api.dto.RegistrationRequest;
import ru.dterenteva.api.entities.CustomGrantedAuthority;
import ru.dterenteva.api.entities.CustomUserDetails;
import ru.dterenteva.api.security.RoleDao;
import ru.dterenteva.api.entities.Role;
import ru.dterenteva.api.entities.User;
import ru.dterenteva.entities.dto.OwnerDto;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserMapperImpl implements UserMapper {
    @NonNull
    private final RoleDao roleDao;
    private final RabbitTemplate template;

    @Override
    public CustomUserDetails userToUserDetails(User user) {
//        user.getRoles();//.stream().map(Role::getName);//.map(x -> new RoleDto(x));
        List<CustomGrantedAuthority> roles = user.getRoles().stream().map(x -> new CustomGrantedAuthority(x.getName())).toList();

        return new CustomUserDetails(user.getName(), user.getPassword(), roles, user.getOwnerId());
    }

    @Override
    public User userDetailsToUser(CustomUserDetails userDetails) {
        User user = new User();
        user.setName(userDetails.getName());
        user.setPassword(userDetails.getPassword());
        List<Role> roles = userDetails.getRoles().stream().map(x -> roleDao.findByName(x.getName())).toList();
        user.setRoles(roles);
        if (userDetails.getOwnerId() != null) {
            OwnerDto owner = template.convertSendAndReceiveAsType("getOwnerQueue", userDetails.getOwnerId(), new ParameterizedTypeReference<OwnerDto>() {});
            if (owner == null) {
                throw new IllegalArgumentException("unknown owner");
            }
            user.setOwnerId(userDetails.getOwnerId());
        } else {
            user.setOwnerId(null);
        }
        return user;
    }

    @Override
    public User RegistrationRequestToUser(RegistrationRequest dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        List<Role> roles = dto.getRoles().stream().map(x -> roleDao.findByName(x.getName())).toList();
        user.setRoles(roles);
        if (dto.getOwnerId() != null) {
            OwnerDto owner = template.convertSendAndReceiveAsType("getOwnerQueue", dto.getOwnerId(), new ParameterizedTypeReference<OwnerDto>() {});
            if (owner == null) {
                throw new IllegalArgumentException("unknown owner");
            }
            user.setOwnerId(dto.getOwnerId());
        } else {
            user.setOwnerId(null);
        }
        return user;
    }
}
