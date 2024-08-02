package ru.dterenteva.api;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfiguration {
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("user");
        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue getOwnerQueue() {
        return new Queue("getOwnerQueue");
    }

    @Bean
    public Queue getCatQueue() {
        return new Queue("getCatQueue");
    }

    @Bean
    public Queue getCatsByBreed() {
        return new Queue("getCatsByBreed");
    }

    @Bean
    public Queue getAllCats() {
        return new Queue("getAllCats");
    }

    @Bean
    public Queue getAllOwners() {
        return new Queue("getAllOwners");
    }

    @Bean
    public Queue addCatQueue() {
        return new Queue("addCatQueue");
    }

    @Bean
    public Queue deleteCat() {
        return new Queue("deleteCat");
    }

    @Bean
    public Queue deleteOwner() {
        return new Queue("deleteOwner");
    }

    @Bean
    public Queue addOwner() {
        return new Queue("addOwner");
    }

    @Bean
    public Queue updateCat() {
        return new Queue("updateCat");
    }

    @Bean Queue updateOwner() {
        return new Queue("updateOwner");
    }
}