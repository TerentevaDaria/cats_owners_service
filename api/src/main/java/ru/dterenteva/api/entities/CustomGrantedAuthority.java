package ru.dterenteva.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}