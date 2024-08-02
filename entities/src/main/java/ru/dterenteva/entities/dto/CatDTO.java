package ru.dterenteva.entities.dto;


import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
@Setter
public class CatDTO implements Serializable {
    @NonNull
    private String name;
    @NonNull
    @PastOrPresent
    Date birthDate;
    @NonNull
    String breed;
    @NonNull
    DtoColor color;
    private Integer ownerId;
    @NonNull
    Set<Integer> friends;
}
