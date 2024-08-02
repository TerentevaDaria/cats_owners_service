package ru.dterenteva.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
//import ru.dterenteva.application.UserService;
import ru.dterenteva.api.dto.RegistrationRequest;
import ru.dterenteva.api.security.UserService;
import ru.dterenteva.entities.dto.CatDTO;
import ru.dterenteva.entities.dto.OwnerDto;
import ru.dterenteva.entities.dto.UpdateCatRequest;
import ru.dterenteva.entities.dto.UpdateOwnerRequest;
//import ru.dterenteva.application.dto.RegistrationRequest;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Set;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@Valid
@EnableRabbit
@Getter
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AppController {
//    @NonNull
//    private final CatService catService;
//    @NonNull
//    private final OwnerService ownerService;
    @NonNull
    private final UserService userService;
    private final RabbitTemplate template;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String return404(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @PreAuthorize("@securityService.hasOwner(#id) || hasAuthority('ROLE_ADMIN')")
    @GetMapping("/owners/{id}")
    public OwnerDto getOwnerInfoById(@PathVariable Integer id) {
        OwnerDto owner = template.convertSendAndReceiveAsType("getOwnerQueue", id, new ParameterizedTypeReference<OwnerDto>() {});
        if (owner == null) {
            throw new IllegalArgumentException("unknown owner");
        }
        return owner;
    }

    @PreAuthorize("@securityService.hasOwner(#id) || hasAuthority('ROLE_ADMIN')")
    @GetMapping("/owners/{id}/cats")
    public List<Integer> getAllCats(@PathVariable Integer id) {
        List<Integer> cats = this.getOwnerInfoById(id).getCats();

        return cats;
    }

    @PreAuthorize("@securityService.hasOwner(#id) || hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/owners/{id}")
    public String deleteOwner(@PathVariable int id) {
        return template.convertSendAndReceiveAsType("deleteOwner", id, new ParameterizedTypeReference<String>() {});
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/owners")
    public int addOwner(@Valid @RequestBody @NonNull OwnerDto dto) {
        Integer id = template.convertSendAndReceiveAsType("addOwner", dto, new ParameterizedTypeReference<Integer>() {});
        if (id == null) {
            throw new IllegalArgumentException("wrong data");
        }

        return id;
    }

//    @PreAuthorize("@securityService.hasOwner(#id) || hasAuthority('ROLE_ADMIN')")
//    @PutMapping("/owners/{ownerId}/cats/{catId}")
//    public void addCat(@PathVariable int ownerId, @PathVariable int catId) {
//        this.ownerService.addCat(ownerId, catId);
//    }
//
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PutMapping("/cats/{id}/friends/{friendId}")
//    public void makeFriendsById(@PathVariable int id, @PathVariable int friendId) {
//        this.catService.makeFriendsById(id, friendId);
//    }
//
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/cats/{id}")
    public CatDTO getCatInfoById(@PathVariable Integer id) {
        CatDTO cat = template.convertSendAndReceiveAsType("getCatQueue", id, new ParameterizedTypeReference<CatDTO>() {});
        //CatDTO cat = (CatDTO) template.convertSendAndReceive("getCatQueue", id);
        if (cat == null) {
            throw new IllegalArgumentException("unknown cat");
        }

        return cat;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/cats/{id}/friends")
    public Set<Integer> getAllFriends(@PathVariable Integer id) {
        Set<Integer> cats = this.getCatInfoById(id).getFriends();

        return cats;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/cats/{id}")
    public String deleteCat(@PathVariable Integer id) {
        return template.convertSendAndReceiveAsType("deleteCat", id, new ParameterizedTypeReference<String>() {});
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/cats")
    public Integer addCat(@Valid @RequestBody @NonNull CatDTO dto) {
        Integer id = template.convertSendAndReceiveAsType("addCatQueue", dto, new ParameterizedTypeReference<Integer>() {});
        if (id == null) {
            throw new IllegalArgumentException("wrong data");
        }

        return id;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/cats/{id}")
    public String updateCat(@Valid @RequestBody @NonNull CatDTO dto, @PathVariable Integer id) {
        UpdateCatRequest request = new UpdateCatRequest(id, dto);
        String res = template.convertSendAndReceiveAsType("updateCat", request, new ParameterizedTypeReference<String>() {});

        return res;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/owners/{id}")
    public String updateOwner(@Valid @RequestBody OwnerDto dto, @PathVariable int id) {
        UpdateOwnerRequest request = new UpdateOwnerRequest(id, dto);
        String res = template.convertSendAndReceiveAsType("updateOwner", request, new ParameterizedTypeReference<String>() {});

        return res;
    }

    @PostAuthorize("@securityService.filterCats(returnObject)")
    @GetMapping("/cats/breed/{breed}")
    public List<CatDTO> getCatsByBreed(@PathVariable String breed) {
        List<CatDTO> cats = template.convertSendAndReceiveAsType("getCatsByBreed", breed, new ParameterizedTypeReference<List<CatDTO>>() {});
        return cats;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/cats")
    public List<CatDTO> getAllCats() {
        List<CatDTO> cats = template.convertSendAndReceiveAsType("getAllCats", "", new ParameterizedTypeReference<List<CatDTO>>() {});
        return cats;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/owners")
    public List<OwnerDto> getAllOwners() {
        List<OwnerDto> owners = template.convertSendAndReceiveAsType("getAllOwners", "", new ParameterizedTypeReference<List<OwnerDto>>() {});
        return owners;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/registration")
    public void addUser(@Valid @RequestBody RegistrationRequest request) {
        this.userService.addUser(request);
    }
}
