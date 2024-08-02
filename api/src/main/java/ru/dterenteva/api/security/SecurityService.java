package ru.dterenteva.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.dterenteva.entities.dto.CatDTO;
import ru.dterenteva.entities.dto.OwnerDto;
import ru.dterenteva.api.entities.CustomUserDetails;

import java.util.Iterator;
import java.util.List;

@Service("securityService")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SecurityService {
    private final UserService userService;
    private Authentication authentication;
    private RabbitTemplate template;

    public boolean hasOwner(int id) {
        OwnerDto owner = template.convertSendAndReceiveAsType("getOwnerQueue", id, new ParameterizedTypeReference<OwnerDto>() {});
        if (owner == null) {
            throw new IllegalArgumentException("unknown owner");
        }
        try {
            CustomUserDetails user = this.userService.getUserByOwnerId(id);
            this.authentication = SecurityContextHolder.getContext().getAuthentication();

            return authentication.getName().equals(user.getUsername()); // || SecurityUtils.isAdmin();
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    public Boolean filterCats(List<CatDTO> cats) {
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = this.userService.getUserByName(this.authentication.getName());
        if (user.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("ROLE_ADMIN"))) {
            return true;
        }

        Iterator<CatDTO> iterator = cats.iterator();
        while (iterator.hasNext()) {
            CatDTO cat = iterator.next();
            if (!cat.getOwnerId().equals(user.getOwnerId())) {
                iterator.remove();
            }
        }

        return true;
    }
}
