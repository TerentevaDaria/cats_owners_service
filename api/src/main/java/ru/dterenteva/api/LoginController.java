package ru.dterenteva.api;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dterenteva.api.dto.LoginRequest;
import ru.dterenteva.api.dto.LoginResponse;

import javax.validation.Valid;

@RestController
@Valid
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LoginController {
    @NonNull
    private final JwtGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String token = jwtGenerator.generateToken(request.getUsername());

        return new LoginResponse(token);
    }
}
