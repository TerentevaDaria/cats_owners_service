package ru.dterenteva.owner.rabbit;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.dterenteva.entities.dto.CatDTO;
import ru.dterenteva.entities.dto.OwnerDto;
import ru.dterenteva.entities.dto.UpdateCatRequest;
import ru.dterenteva.entities.dto.UpdateOwnerRequest;
import ru.dterenteva.owner.service.OwnerService;

import java.util.Date;
import java.util.List;

@Component
//@EnableRabbit
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class RabbitMqListener {
    private final OwnerService ownerService;
    private final RabbitTemplate template;

    @RabbitListener(queues = "getOwnerQueue")
    public OwnerDto getOwnerById(@Payload Integer id) {
        try {
            return ownerService.getOwnerInfoById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @RabbitListener(queues = "getAllOwners")
    public List<OwnerDto> getAllOwners() {
        try {
            return ownerService.getAllOwners();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RabbitListener(queues = "deleteOwner")
    public String deleteOwner(@Payload Integer id) {
        try {
            OwnerDto owner = this.getOwnerById(id);
            if (owner == null) {
                return null;
            }

            for (Integer catId : owner.getCats()) {
                CatDTO cat = template.convertSendAndReceiveAsType("getCatQueue", catId, new ParameterizedTypeReference<CatDTO>() {});
                if (cat != null) {
                    cat.setOwnerId(null);
                    UpdateCatRequest request = new UpdateCatRequest(catId, cat);
                    String res = template.convertSendAndReceiveAsType("updateCat", request, new ParameterizedTypeReference<String>() {});
                }
            }

            return ownerService.deleteOwner(id);
        } catch (IllegalArgumentException e) {
            return "unknown id";
        }
    }

    @RabbitListener(queues = "addOwner")
    public Integer addOwner(@Payload OwnerDto owner) {
        try {
            Integer id = ownerService.addOwner(owner);

            for (Integer catId : owner.getCats()) {
                CatDTO cat = template.convertSendAndReceiveAsType("getCatQueue", catId, new ParameterizedTypeReference<CatDTO>() {});
                if (cat != null) {
                    cat.setOwnerId(id);
                    UpdateCatRequest request = new UpdateCatRequest(catId, cat);
                    String res = template.convertSendAndReceiveAsType("updateCat", request, new ParameterizedTypeReference<String>() {});
                }
            }

            return id;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RabbitListener(queues = "updateOwner")
    public String updateOwner(@Payload UpdateOwnerRequest request) {
        try {
            OwnerDto id = ownerService.getOwnerInfoById(request.getId());
            if (id == null) {
                return "unknown id";
            }

            for (Integer catId : request.getOwner().getCats()) {
                CatDTO cat = template.convertSendAndReceiveAsType("getCatQueue", catId, new ParameterizedTypeReference<CatDTO>() {});
                if (cat != null) {
                    cat.setOwnerId(request.getId());
                    UpdateCatRequest request2 = new UpdateCatRequest(catId, cat);
                    String res = template.convertSendAndReceiveAsType("updateCat", request2, new ParameterizedTypeReference<String>() {});
                }
            }

            this.ownerService.updateOwner(request.getId(), request.getOwner());

            return "success";
        } catch (IllegalArgumentException e) {
            return "wrong data";
        }
    }
}