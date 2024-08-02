package ru.dterenteva.owner.mapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.dterenteva.entities.dto.CatDTO;
import ru.dterenteva.entities.dto.OwnerDto;
import ru.dterenteva.owner.dao.OwnerDao;
import ru.dterenteva.owner.dao.Owner;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableRabbit
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class OwnerMapperImpl implements OwnerMapper {
    @NonNull
    private final OwnerDao ownerDAO;
    private final RabbitTemplate template;

    @Override
    public Owner dtoToOwner(OwnerDto ownerDTO) {
        Owner owner = new Owner();
        owner.setName(ownerDTO.getName());
        owner.setBirthDate(ownerDTO.getBirthDate());
        // owner.setCats(ownerDTO.getCats()); //.stream().map(this.catDao::findById).map(x -> x.orElseThrow(() -> new IllegalArgumentException("unknown cat"))).toList());
        // owner.setUserId(userDao.findByName(ownerDTO.getUserName()).orElseThrow(() -> new IllegalArgumentException("unknown user")));
        return owner;
    }

    @Override
    public OwnerDto ownerToDTO(Owner owner) {
        List<Integer> cats = new ArrayList<>();
        try {
            cats = template.convertSendAndReceiveAsType("getCatsByOwner", owner.getId(), new ParameterizedTypeReference<List<Integer>>() {});
            if (cats == null) {
                cats = new ArrayList<Integer>();
            }
        } catch (Exception e) {
            cats = new ArrayList<>();
        }
        //List<Integer> cats = owner.getCats().stream().map(Cat::getId).toList();
        return new OwnerDto(owner.getName(), owner.getBirthDate(), cats); // , cats, owner.getUser());
    }
}
