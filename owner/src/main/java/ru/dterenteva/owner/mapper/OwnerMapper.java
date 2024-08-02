package ru.dterenteva.owner.mapper;

import ru.dterenteva.entities.dto.OwnerDto;
import ru.dterenteva.owner.dao.Owner;

public interface OwnerMapper {
    public Owner dtoToOwner(OwnerDto ownerDTO);
    public OwnerDto ownerToDTO(Owner owner);
}
