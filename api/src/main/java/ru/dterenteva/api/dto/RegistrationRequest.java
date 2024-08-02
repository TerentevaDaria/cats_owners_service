package ru.dterenteva.api.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dterenteva.api.entities.CustomGrantedAuthority;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
public class RegistrationRequest {
    @Nonnull
    @NotBlank
    public String name;

    @NotBlank
    public String password;

    @Nonnull
    public List<CustomGrantedAuthority> roles;

    public Integer ownerId;
}
