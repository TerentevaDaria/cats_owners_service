package ru.dterenteva.owner;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.dterenteva.owner")
@ComponentScan(basePackages = "ru.dterenteva.owner")
@EntityScan(basePackages = "ru.dterenteva.owner")
public class ApplicationConfiguration {
}
