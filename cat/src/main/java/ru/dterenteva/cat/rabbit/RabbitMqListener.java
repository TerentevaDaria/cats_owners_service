package ru.dterenteva.cat.rabbit;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.dterenteva.entities.dto.CatDTO;
import ru.dterenteva.cat.service.CatService;
import ru.dterenteva.entities.dto.UpdateCatRequest;

import java.util.List;

@Component
//@EnableRabbit
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class RabbitMqListener {
    private final CatService catService;

    @RabbitListener(queues = "getCatQueue")
    public CatDTO getCatById(@Payload Integer id) {
        try {
            return catService.getCatInfoById(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RabbitListener(queues = "getCatsByOwner")
    public List<Integer> getCatsByOwner(@Payload Integer id) {
        try {
            return catService.getCatsByOwnerId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RabbitListener(queues = "getCatsByBreed")
    public List<CatDTO> getCatsByBreed(@Payload String breed) {
        try {
            return catService.getCatsByBreed(breed);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RabbitListener(queues = "getAllCats")
    public List<CatDTO> getAllCats() {
        try {
            return catService.getAllCats();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RabbitListener(queues = "addCatQueue")
    public Integer addCat(@Payload CatDTO cat) {
        try {
            return catService.addCat(cat);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RabbitListener(queues = "deleteCat")
    public String deleteCat(@Payload Integer id) {
        try {
            return catService.deleteCat(id);
        } catch (IllegalArgumentException e) {
            return "unknown id";
        }
    }

    @RabbitListener(queues = "updateCat")
    public String updateCat(@Payload UpdateCatRequest request) {
    //public String updateCat(@Payload CatDTO request) {

        try {
            //catService.updateCat(52, request);
            catService.updateCat(request.getId(), request.getCat());
            return "success";
        } catch (Exception e) {
            return "wrong data";
        }
    }
}
