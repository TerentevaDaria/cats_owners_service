package ru.dterenteva.owner.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.dterenteva.entities.dto.OwnerDto;
import ru.dterenteva.owner.mapper.OwnerMapper;
import ru.dterenteva.owner.dao.OwnerDao;
//import ru.dterenteva.entities.entity.Cat;
import ru.dterenteva.owner.dao.Owner;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class OwnerService {
    private final OwnerDao ownerDAO;
    private final OwnerMapper mapper;

    public OwnerDto getOwnerInfoById(int id) {
        try {
            Owner owner = this.ownerDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("unknown owner with Id " + Integer.toString(id)));
            return this.mapper.ownerToDTO(owner);
        } catch (Exception e) {
            return null;
        }
    }
//
//    public List<Integer> getAllCats(int id) {
//        Owner owner = this.ownerDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("unknown owner with Id " + Integer.toString(id)));
//
//        return this.mapper.ownerToDTO(owner).getCats();
//    }

    public String deleteOwner(Integer id) {
        Owner owner = this.ownerDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("unknown owner with Id " + Integer.toString(id)));

        ownerDAO.delete(owner);

        return "success";
    }

    public int addOwner(@NonNull OwnerDto dto) {
        Owner owner = mapper.dtoToOwner(dto);
        ownerDAO.save(owner);

        return owner.getId();
    }

//    public void addCat(int ownerId, int catId) {
//        Cat cat = catDAO.findById(catId).orElseThrow(() -> new IllegalArgumentException("unknown cat" + Integer.toString(catId)));
//        Owner owner = this.ownerDAO.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("unknown owner with Id " + Integer.toString(ownerId)));
//
//        cat.setOwner(owner);
//        catDAO.save(cat);
//    }
//
    public void updateOwner(int id, @NonNull OwnerDto dto) {
        this.ownerDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("unknown owner with Id " + Integer.toString(id)));

        Owner owner = mapper.dtoToOwner(dto);
        owner.setId(id);
        ownerDAO.save(owner);
    }

    public List<OwnerDto> getAllOwners() {
        return ownerDAO.findAll().stream().map(this.mapper::ownerToDTO).toList();
    }
}
