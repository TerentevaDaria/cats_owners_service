package ru.dterenteva.api.security;

import ru.dterenteva.api.dto.RegistrationRequest;
import ru.dterenteva.api.entities.CustomUserDetails;
import ru.dterenteva.api.entities.User;

public interface UserMapper {
    public User userDetailsToUser(CustomUserDetails dto);
    public CustomUserDetails userToUserDetails(User user);
    public User RegistrationRequestToUser(RegistrationRequest dto);
}
