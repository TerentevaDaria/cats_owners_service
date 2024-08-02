package ru.dterenteva.cat.mapper;

import ru.dterenteva.entities.dto.CatDTO;
import ru.dterenteva.cat.dao.Cat;

public interface CatMapper {
    public Cat dtoToCat(CatDTO catDTO);
    public CatDTO catToDTO(Cat cat);
}