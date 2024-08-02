package ru.dterenteva.cat;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;

@Configuration
@EnableRabbit
public class RabbitConfiguration {
    public static final String EXCHANGE_NAME = "ownerExchange";

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
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
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
    public Queue getCatsByOwner() {
        return new Queue("getCatsByOwner");
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
    public Queue addCatQueue() {
        return new Queue("addCatQueue");
    }

    @Bean
    public Queue deleteCat() {
        return new Queue("deleteCat");
    }

    @Bean
    public Queue updateCat() {
        return new Queue("updateCat");
    }
}