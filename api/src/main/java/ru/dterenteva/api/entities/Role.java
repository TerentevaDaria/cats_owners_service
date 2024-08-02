package ru.dterenteva.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "roles")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Role { // implements GrantedAuthority {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch=FetchType.LAZY)
    private List<User> users;

//    @Override
//    public String getAuthority() {
//        return getName();
//    }
}
