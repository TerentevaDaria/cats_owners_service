package ru.dterenteva.entities.dto;

import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
public class OwnerDto implements Serializable {
    @NonNull private String name;
    @NonNull @PastOrPresent Date birthDate;
    @NonNull List<Integer> cats;
    //private String userName;
}
