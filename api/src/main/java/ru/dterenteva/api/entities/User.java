package ru.dterenteva.api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User { // implements UserDetails {
    @Id
    @GeneratedValue
    public int id;

    @Column(unique = true, nullable = false)
    public String name;

    @Column(nullable = false)
    public String password;

    @ManyToMany(fetch = FetchType.EAGER)
    public List<Role> roles;

//    @OneToOne(fetch = FetchType.EAGER)
//    public Owner owner;
    private Integer ownerId;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return getRoles();
//    }
//
//    @Override
//    public String getUsername() {
//        return name;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
