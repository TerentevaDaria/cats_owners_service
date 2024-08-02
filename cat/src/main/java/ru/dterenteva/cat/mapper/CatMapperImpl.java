package ru.dterenteva.cat.mapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dterenteva.entities.dto.CatDTO;
import ru.dterenteva.entities.dto.DtoColor;
import ru.dterenteva.cat.dao.CatDao;
import ru.dterenteva.cat.dao.Cat;
import ru.dterenteva.cat.dao.Color;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class CatMapperImpl implements CatMapper {
    @NonNull
    private final CatDao catDao;

    @Override
    public Cat dtoToCat(@NonNull CatDTO catDTO) {
        Cat cat = new Cat();
        cat.setBirthDate(catDTO.getBirthDate());
        cat.setName(catDTO.getName());

        switch (catDTO.getColor()) {
            case BLACK:
                cat.setColor(Color.BLACK);
                break;
            case WHITE:
                cat.setColor(Color.WHITE);
                break;
            default:
                cat.setColor(Color.ANOTHER);
                break;
        }
        cat.setBreed(catDTO.getBreed());
        cat.setOwnerId(catDTO.getOwnerId());
        cat.setFriends(catDTO.getFriends().stream().map(catDao::findById).map(x -> x.orElseThrow(() -> new IllegalArgumentException("unknown cat"))).collect(Collectors.toSet()));

        return cat;
    }

    @Override
    public CatDTO catToDTO(@NonNull Cat cat) {
        Set<Integer> friends = cat.getFriends().stream().map(Cat::getId).collect(Collectors.toSet());

        DtoColor color;
        switch (cat.getColor()) {
            case BLACK:
                color = DtoColor.BLACK;
                break;
            case WHITE:
                color = DtoColor.WHITE;
                break;
            default:
                color = DtoColor.ANOTHER;
                break;
        }

        return new CatDTO(cat.getName(), cat.getBirthDate(), cat.getBreed(), color, cat.getOwnerId(), friends);
    }
}
