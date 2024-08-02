package ru.dterenteva.api;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.dterenteva.api.security.RoleService;
import ru.dterenteva.api.security.UserService;
import ru.dterenteva.api.dto.RegistrationRequest;
import ru.dterenteva.api.entities.CustomGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
@AllArgsConstructor(onConstructor_ = {@Autowired})
@ConfigurationPropertiesScan(basePackages = "ru.dterenteva.api")
public class WebSecurityConfig {
    private UserService userDetailsService;
    private RoleService roleService;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
//
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        Set permissions on endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyRequest().authenticated()//)
                )
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()//)
//                )
                .authenticationManager(authenticationManager)
//                .logout(logout -> logout
//                .logoutUrl("/logout")
//                .addLogoutHandler(new SecurityContextLogoutHandler()))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CommandLineRunner loadData() {
        return (args) -> {
            List<CustomGrantedAuthority> roles = new ArrayList<>();
            roles.add(new CustomGrantedAuthority("ROLE_ADMIN"));
            roles.add(new CustomGrantedAuthority("ROLE_USER"));
            roleService.addRole(new CustomGrantedAuthority("ROLE_ADMIN"));
            roleService.addRole(new CustomGrantedAuthority("ROLE_USER"));
            RegistrationRequest admin = new RegistrationRequest("user", "user", roles, null);

            userDetailsService.addUser(admin);
        };
    }
}
