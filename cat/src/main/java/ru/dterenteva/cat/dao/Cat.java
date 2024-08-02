package ru.dterenteva.cat.dao;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "cats")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Cat {
    @Id
    @GeneratedValue
    private int id;

    private String name;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private String breed;

    @Enumerated(EnumType.STRING)
    private Color color;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@OnDelete(action = OnDeleteAction.SET_NULL)
    //private Owner owner;
    private Integer ownerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cats_cats",
            joinColumns = {@JoinColumn(name = "cat_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")}
    )
    private Set<Cat> friends;
}
