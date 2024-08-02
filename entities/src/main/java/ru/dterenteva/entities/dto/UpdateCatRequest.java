package ru.dterenteva.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UpdateCatRequest implements Serializable {
    private Integer id;
    private CatDTO cat;
}
