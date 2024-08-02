package ru.dterenteva.cat.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.dterenteva.entities.dto.CatDTO;
import ru.dterenteva.cat.mapper.CatMapper;
import ru.dterenteva.cat.dao.CatDao;
import ru.dterenteva.entities.dto.OwnerDto;
import ru.dterenteva.cat.dao.Cat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class CatService {
    private final CatDao catDAO;
    private final CatMapper mapper;
    private final RabbitTemplate template;

    public void makeFriendsById(int friendId1, int friendId2) {
        if (friendId1 == friendId2) {
            throw new UnsupportedOperationException("Can't make friends by same ids");
        }

        Cat cat1 = catDAO.findById(friendId1).orElseThrow(() -> new IllegalArgumentException("unknown cat" + Integer.toString(friendId1)));

        Cat cat2 = catDAO.findById(friendId2).orElseThrow(() -> new IllegalArgumentException("unknown cat" + Integer.toString(friendId2)));

        cat1.getFriends().add(cat2);
        cat2.getFriends().add(cat1);
        catDAO.save(cat1);
        catDAO.save(cat2);
    }

    public CatDTO getCatInfoById(int id) {
        Cat cat = catDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("unknown cat" + Integer.toString(id)));

        return this.mapper.catToDTO(cat);
    }

    public Set<Integer> getAllFriends(int id) {
        Cat cat = catDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("unknown cat" + Integer.toString(id)));

        return this.mapper.catToDTO(cat).getFriends();
    }

    public String deleteCat(int id) {
        Cat cat = catDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("unknown cat" + Integer.toString(id)));

        catDAO.delete(cat);

        return "success";
    }

    public int addCat(@Valid @NonNull CatDTO dto) {
        Cat cat = mapper.dtoToCat(dto);

        if (!this.isOwnerCorrect(dto.getOwnerId())) {
            throw new IllegalArgumentException("unknown owner");
        }

        catDAO.save(cat);

        return cat.getId();
    }

    public void updateCat(Integer id, @NonNull CatDTO dto) {
        catDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("unknown cat" + Integer.toString(id)));

        if (!this.isOwnerCorrect(dto.getOwnerId())) {
            throw new IllegalArgumentException("unknown owner");
        }

        Cat cat = mapper.dtoToCat(dto);
        cat.setId(id);
        catDAO.save(cat);
    }

    public List<CatDTO> getCatsByBreed(String breed) {
        return this.catDAO.findCatsByBreed(breed).stream().map(this.mapper::catToDTO).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Integer> getCatsByOwnerId(Integer id) {
        return this.catDAO.findCatsByOwnerId(id).stream().map(Cat::getId).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<CatDTO> getAllCats() {
        return this.catDAO.findAll().stream().map(this.mapper::catToDTO).toList();
    }

    public boolean isOwnerCorrect(Integer ownerId) {
        if (ownerId == null) return true;

        OwnerDto owner = template.convertSendAndReceiveAsType("getOwnerQueue", ownerId, new ParameterizedTypeReference<OwnerDto>() {});
        return owner != null;
    }
}
