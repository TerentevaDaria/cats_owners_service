package ru.dterenteva.owner.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Entity
@Table(name = "owners")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Owner {
    @Id
    @GeneratedValue
    private int id;

    private String name;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @PastOrPresent
    private Date birthDate;

//    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
//    private List<Cat> cats;

//    @OneToOne(mappedBy = "owner", fetch = FetchType.EAGER)
//    private User user;
    //private String userName;
}
